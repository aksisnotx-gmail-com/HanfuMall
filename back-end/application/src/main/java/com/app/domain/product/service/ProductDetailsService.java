package com.app.domain.product.service;

import com.app.domain.base.AbstractService;
import com.app.domain.product.entity.ProductDetailsEntity;
import com.app.domain.product.mapper.ProductDetailsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author xxl
 * @since 2024/3/19
 */
@Service
@RequiredArgsConstructor
public class ProductDetailsService extends AbstractService<ProductDetailsMapper,ProductDetailsEntity> {

    private final ProductSkuService skuService;



}
