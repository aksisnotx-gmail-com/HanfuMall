package com.app.domain.product.entity;

import com.app.domain.base.Entity;
import com.app.domain.product.enums.ShippingType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 商品详情(SysProductDetails)表实体类
 *
 * @author makejava
 * @since 2024-03-19 15:31:45
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "商品详情实体")
public class ProductDetailsEntity extends Entity {

    @Serial
    private static final long serialVersionUID = 5910473738974612741L;

    //轮播图来自于sku
    @Schema(description = "轮播图")
    private String carousel;

    //发货地址
    @Schema(description = "发货地址")
    private String deliveryAddress;

    //商品名称
    @Schema(description = "商品名称")
    private String productName;

    //发货类型
    @Schema(description = "发货类型")
    private ShippingType shippingType;

    //商品描述,图片都是为json
    @Schema(description = "商品描述")
    private String productDescription;

    //商品类型：汉服、首饰
    @Schema(description = "商品类型")
    private String productTypes;

    //折扣
    @Schema(description = "折扣")
    private Double discount;
}

