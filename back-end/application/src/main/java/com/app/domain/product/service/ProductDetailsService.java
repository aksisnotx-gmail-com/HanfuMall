package com.app.domain.product.service;

import cn.hutool.core.bean.BeanUtil;
import com.app.domain.base.AbstractService;
import com.app.domain.base.Entity;
import com.app.domain.product.entity.ProductDetailsEntity;
import com.app.domain.product.entity.ProductSkuEntity;
import com.app.domain.product.entity.ProductTypeEntity;
import com.app.domain.product.mapper.ProductDetailsMapper;
import com.app.domain.product.param.ProductDetailModifyParam;
import com.app.domain.product.param.ProductDetailParam;
import com.app.domain.product.param.ProductSizeModifyParam;
import com.app.domain.product.param.vo.ProductVO;
import com.app.domain.product.param.vo.RecommendProductVO;
import com.app.toolkit.web.CommonPageRequestUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sdk.util.asserts.AssertUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author xxl
 * @since 2024/3/19
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductDetailsService extends AbstractService<ProductDetailsMapper,ProductDetailsEntity> {

    private final ProductSkuService skuService;

    public static final String PRODUCT = "PRODUCT";

    public static final String SKU = "SKU";

    //是否特惠
    private static final Integer IS_SPECIAL = 1;

    //是否推荐
    private static final Integer IS_RECOMMEND = 1;

    private final ProductTypeService typeService;

    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean publishDetail(ProductDetailParam param) {
        ProductDetailsEntity entity = new ProductDetailsEntity();
        BeanUtil.copyProperties(param,entity);
        typeService.getProductType(entity.getProductTypeIds());
        return this.saveOrUpdateBatchAround(List.of(entity), Entity::getId,null,(t1, t2, t3)-> {
            //把尺码信息插入到sku表
            List<ProductSkuEntity> list = param.getSkus().stream().map(t -> {
                ProductSkuEntity sku = ProductSkuEntity.create(t);
                sku.setProductId(t2.getId());
                return sku;
            }).toList();
            //插入
            skuService.saveBatch(list);
        });
    }

    public ProductVO getDetail(String productId) {
        ProductDetailsEntity productDetail = this.getById(productId);
        AssertUtils.notNull(productDetail,"商品详情不存在");
        //转换商品类型
        productDetail.setProductTypeIds(typeService.getProductType(productDetail.getProductTypeIds()).stream().map(ProductTypeEntity::getType).toList());
        //商品尺码信息
        List<ProductSkuEntity> list = skuService.lambdaQuery().eq(ProductSkuEntity::getProductId, productId).list();
        return ProductVO.create(productDetail,list);
    }

    public Page<ProductVO> getAllDetail() {
        Page<ProductDetailsEntity> page = this.page(CommonPageRequestUtils.defaultPage());
        Page<ProductVO> voPage = entityPageToVoPage(page);
        //把entity转换为vo
        List<ProductVO> list = page.getRecords().stream().map(t -> getDetail(t.getId())).toList();
        voPage.setRecords(list);
        return voPage;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean deleteProductById(List<String> ids) {
        return this.removeBatchByIds(ids);
    }

    public Boolean modifyDetail(ProductDetailModifyParam param) {
        ProductDetailsEntity entity = this.getById(param.getId());
        AssertUtils.notNull(entity,"商品详情不存在");
        BeanUtil.copyProperties(param,entity);
        //校验所有的类型是否存在
        typeService.getProductType(entity.getProductTypeIds());
        return  this.updateById(entity);
    }

    public Boolean deleteDetailSize(String productId, String sizeId) {
        return skuService.lambdaUpdate().eq(ProductSkuEntity::getProductId,productId).eq(ProductSkuEntity::getId,sizeId).remove();
    }

    public Boolean modifyDetailSize(ProductSizeModifyParam param) {
        ProductSkuEntity one = skuService.lambdaQuery().eq(ProductSkuEntity::getProductId, param.getProductId()).eq(ProductSkuEntity::getId, param.getId()).one();
        AssertUtils.notNull(one,"商品尺码不存在");
        BeanUtil.copyProperties(param,one);
        one.setAttribute(param.getStyle());
        one.setPrice(param.getStyle().getPrice());
        return skuService.updateById(one);
    }

    public Boolean addDetailSize(ProductSizeModifyParam param) {
        AssertUtils.notNull(this.getById(param.getProductId()),"商品不能为空");
        ProductSkuEntity sku = new ProductSkuEntity();
        BeanUtil.copyProperties(param, sku);
        sku.setAttribute(param.getStyle());
        sku.setPrice(param.getStyle().getPrice());
        return skuService.save(sku);
    }

    public Page<ProductVO> getDetailByType(String typeId) {
        //获取产品类型
        ProductTypeEntity entity = typeService.getById(typeId);
        Page<ProductVO> detail = getAllDetail();
        List<ProductVO> list = detail.getRecords().stream().filter(t -> t.getProductTypes().contains(entity.getType())).toList();
        detail.setTotal(list.size());
        detail.setRecords(list);
        return detail;
    }

    public Page<ProductVO> search(String productName) {
        Page<ProductDetailsEntity> page = this.lambdaQuery().like(ProductDetailsEntity::getProductName, productName).page(CommonPageRequestUtils.defaultPage());
        Page<ProductVO> voPage = entityPageToVoPage(page);
        //把entity转换为vo
        List<ProductVO> list = page.getRecords().stream().map(t -> getDetail(t.getId())).toList();
        voPage.setRecords(list);
        return voPage;
    }

    public Map<String, Object> getProductBySkuId(String skuId) {
        //获取SKU信息
        ProductSkuEntity sku = skuService.getById(skuId,false);
        if (Objects.isNull(sku)) {
            return null;
        }
        //获取产品信息
        ProductDetailsEntity entity = this.getById(sku.getProductId());
        return Map.of(PRODUCT,entity,SKU,sku);
    }

    public Page<ProductSkuEntity> getSpecialProducts() {
        return skuService.lambdaQuery().eq(ProductSkuEntity::getIsSpecial,IS_SPECIAL).page(CommonPageRequestUtils.defaultPage());
    }

    public Page<RecommendProductVO> getRecommendProducts() {
        Page<ProductSkuEntity> page = skuService.lambdaQuery().eq(ProductSkuEntity::getIsRecommend, IS_RECOMMEND).page(CommonPageRequestUtils.defaultPage());
        List<RecommendProductVO> list = page.getRecords().stream().map(t -> {
            ProductDetailsEntity productDetails = this.getById(t.getProductId());
            return RecommendProductVO.create(t, productDetails);
        }).toList();

        //转换为VO
        Page<RecommendProductVO> productDetailsEntityPage = new Page<>();
        BeanUtil.copyProperties(page,productDetailsEntityPage);
        productDetailsEntityPage.setRecords(list);
        return productDetailsEntityPage;
    }

    private Page<ProductVO> entityPageToVoPage(Page<ProductDetailsEntity> page) {
        Page<ProductVO> voPage = new Page<>();
        BeanUtil.copyProperties(page,voPage);
        return voPage;
    }


    public Boolean deleteType(String typeId) {
        ProductTypeEntity entity = typeService.getById(typeId);
        //找到包含这个ID的
        List<ProductDetailsEntity> list = this.lambdaQuery().like(ProductDetailsEntity::getProductTypeIds, entity.getId()).list();
        //删除这个商品或者更新
        list.stream().peek(t -> {
            List<String> typeIds = t.getProductTypeIds();
            typeIds.remove(entity.getId());
            t.setProductTypeIds(typeIds);
        }).toList().forEach(t -> {
            List<String> typeIds = t.getProductTypeIds();
            if (typeIds.isEmpty()) {
                this.removeById(t);
            }else {
                this.updateById(t);
            }
        });
        return typeService.removeById(typeId);
    }
}
