package com.app.domain.product.service;

import cn.hutool.core.bean.BeanUtil;
import com.app.domain.base.AbstractService;
import com.app.domain.base.Entity;
import com.app.domain.product.entity.ProductDetailsEntity;
import com.app.domain.product.entity.ProductSkuEntity;
import com.app.domain.product.mapper.ProductDetailsMapper;
import com.app.domain.product.param.ProductDetailModifyParam;
import com.app.domain.product.param.ProductDetailParam;
import com.app.domain.product.param.ProductSizeModifyParam;
import com.app.domain.product.param.vo.ProductVO;
import com.app.toolkit.web.CommonPageRequestUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sdk.util.asserts.AssertUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.app.domain.product.service.ProductDetailsService.CACHE_KEY;

/**
 * @author xxl
 * @since 2024/3/19
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = CACHE_KEY)
public class ProductDetailsService extends AbstractService<ProductDetailsMapper,ProductDetailsEntity> {

    public static final String CACHE_KEY = "PRODUCT_DETAIL";

    private final ProductSkuService skuService;

    @Transactional(rollbackFor = RuntimeException.class)
    @CacheEvict(allEntries = true)
    public Boolean publishDetail(ProductDetailParam param) {
        ProductDetailsEntity entity = new ProductDetailsEntity();
        BeanUtil.copyProperties(param,entity);
        //设置图片
        entity.setCarousels(param.getCarousel());
        //设置图片
        entity.setProductDesc(param.getDescUrls());
        //设置产品类型
        entity.setProductType(param.getProductTypes());
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

    @Cacheable(key = "#productId")
    public ProductVO getDetail(String productId) {
        ProductDetailsEntity productDetail = this.getById(productId);
        AssertUtils.notNull(productDetail,"商品详情不存在");
        //商品尺码信息
        List<ProductSkuEntity> list = skuService.lambdaQuery().eq(ProductSkuEntity::getProductId, productId).list();
        return ProductVO.create(productDetail,list);
    }

    @Cacheable(cacheNames = CACHE_KEY)
    public Page<ProductVO> getAllDetail() {
        Page<ProductDetailsEntity> page = this.page(CommonPageRequestUtils.defaultPage());
        Page<ProductVO> voPage = new Page<>();
        BeanUtil.copyProperties(page,voPage);
        //把entity转换为vo
        List<ProductVO> list = page.getRecords().stream().map(t -> getDetail(t.getId())).toList();
        voPage.setRecords(list);
        return voPage;
    }

    @CacheEvict(allEntries = true)
    public Boolean deleteProductById(String id) {
        return this.removeById(id);
    }

    @CacheEvict(allEntries = true)
    public Boolean modifyDetail(ProductDetailModifyParam param) {
        ProductDetailsEntity entity = this.getById(param.getId());
        AssertUtils.notNull(entity,"商品详情不存在");
        BeanUtil.copyProperties(param,entity);
        entity.setCarousels(param.getCarousel());
        entity.setProductDesc(param.getDescUrls());
        entity.setProductType(param.getProductTypes());
        return  this.updateById(entity);
    }

    @CacheEvict(allEntries = true)
    public Boolean deleteDetailSize(String productId, String sizeId) {
        return skuService.lambdaUpdate().eq(ProductSkuEntity::getProductId,productId).eq(ProductSkuEntity::getId,sizeId).remove();
    }

    @CacheEvict(allEntries = true)
    public Boolean modifyDetailSize(ProductSizeModifyParam param) {
        ProductSkuEntity one = skuService.lambdaQuery().eq(ProductSkuEntity::getProductId, param.getProductId()).eq(ProductSkuEntity::getId, param.getId()).one();
        AssertUtils.notNull(one,"商品尺码不存在");
        BeanUtil.copyProperties(param,one);
        one.setAttribute(param.getStyle());
        return skuService.updateById(one);
    }

    @CacheEvict(allEntries = true)
    public Boolean addDetailSize(ProductSizeModifyParam param) {
        AssertUtils.notNull(this.getById(param.getProductId()),"商品不能为空");
        ProductSkuEntity sku = new ProductSkuEntity();
        BeanUtil.copyProperties(param, sku);
        sku.setAttribute(param.getStyle());
        return skuService.save(sku);
    }
}
