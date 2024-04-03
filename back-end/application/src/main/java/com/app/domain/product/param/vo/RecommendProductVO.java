package com.app.domain.product.param.vo;

import com.app.domain.product.entity.ProductDetailsEntity;
import com.app.domain.product.entity.ProductSkuEntity;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author xxl
 * @since 2024/4/3
 */
@Data
public class RecommendProductVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 4683122773718496508L;

    private ProductSkuEntity sku;

    private ProductDetailsEntity product;

    public static RecommendProductVO create( ProductSkuEntity sku, ProductDetailsEntity product) {
        RecommendProductVO productVO = new RecommendProductVO();
        productVO.setSku(sku);
        productVO.setProduct(product);
        return productVO;
    }
}
