package com.app.domain.product.service;

import cn.hutool.core.bean.BeanUtil;
import com.app.domain.base.AbstractService;
import com.app.domain.base.Entity;
import com.app.domain.product.entity.ProductDetailsEntity;
import com.app.domain.product.entity.ProductSkuEntity;
import com.app.domain.product.mapper.ProductDetailsMapper;
import com.app.domain.product.param.ProductDetailParam;
import com.app.domain.product.param.vo.ProductVO;
import com.app.toolkit.web.CommonPageRequestUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sdk.util.asserts.AssertUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author xxl
 * @since 2024/3/19
 */
@Service
@RequiredArgsConstructor
public class ProductDetailsService extends AbstractService<ProductDetailsMapper,ProductDetailsEntity> {

    private final ProductSkuService skuService;

    @Transactional(rollbackFor = RuntimeException.class)
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

    public ProductVO getDetail(String productId) {
        ProductDetailsEntity productDetail = this.getById(productId);
        AssertUtils.notNull(productDetail,"商品详情不存在");
        //商品尺码信息
        List<ProductSkuEntity> list = skuService.lambdaQuery().eq(ProductSkuEntity::getProductId, productId).list();
        ProductVO vo = ProductVO.create(productDetail,list);
        return vo;
    }

    public Page<ProductVO> getAllDetail() {
        Page<ProductDetailsEntity> page = this.page(CommonPageRequestUtils.defaultPage());
        Page<ProductVO> voPage = new Page<>();
        BeanUtil.copyProperties(page,voPage);
        //把entity转换为vo
        List<ProductVO> list = page.getRecords().stream().map(t -> getDetail(t.getId())).toList();
        voPage.setRecords(list);
        return voPage;
    }
}
