package com.app.domain.product.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.app.domain.base.AbstractService;
import com.app.domain.base.Entity;
import com.app.domain.product.entity.ProductDetailsEntity;
import com.app.domain.product.entity.ProductSkuEntity;
import com.app.domain.product.mapper.ProductDetailsMapper;
import com.app.domain.product.param.ProductDetailParam;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
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

    @Transactional
    public Boolean publishDetail(ProductDetailParam param) {
        ProductDetailsEntity entity = new ProductDetailsEntity();
        BeanUtil.copyProperties(param,entity);
        //设置图片
        entity.setProductDesc(param.getDescUrls());
        //设置产品类型
        entity.setProductType(param.getProductTypes());
        return this.saveOrUpdateBatchAround(List.of(entity), Entity::getId,null,(t1, t2, t3)-> {
            //把尺码信息插入到sku表
            param.getSku().stream().map(t -> {
                ProductSkuEntity sku = new ProductSkuEntity();
                ProductDetailParam.ProductStyle style = new ProductDetailParam.ProductStyle();
                sku.setProductId(t2.getId());
                style.setDesc(t.getDesc());
                style.setCarouselUrl(t.getCarouselUrl());
                sku.setAttribute(style);
                //todo
                return sku;
            });
        });
    }
}
