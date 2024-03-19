package com.app.domain.product.param;

import com.app.domain.product.enums.ProductType;
import com.app.domain.product.enums.ShippingType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author xxl
 * @since 2024/3/19
 */
@Data
@Schema(description = "商品详情参数")
public class ProductDetailParam implements Serializable {

    @Serial
    private static final long serialVersionUID = 8571934096526803581L;

    //发货地址
    @Schema(description = "发货地址")
    @NotBlank(message = "发货地址不能为空")
    private String deliveryAddress;

    //商品名称
    @NotBlank(message = "商品名称不能为空")
    @Schema(description = "商品名称")
    private String productName;

    //发货类型
    @Schema(description = "发货类型")
    @NotBlank(message = "发货类型不能为空")
    private ShippingType shippingType;

    //商品描述,图片都是为json
    @Schema(description = "商品描述")
    @NotBlank(message = "商品描述不能为空")
    private String productDescription;

    //商品类型：汉服、首饰
    @Schema(description = "商品类型")
    private List<ProductType> productTypes;

    //折扣
    @Schema(description = "折扣")
    private Double discount;

    /**
     * sku 尺码信息
     */
    @Schema(description = "sku")
    @Data
    public static class Sku {

        @Schema(description = "图片")
        private String carouselUrl;

        //价格
        @Schema(description = "价格")
        private Double price;

        //库存
        @Schema(description = "库存")
        private Integer stock;

        //尺码
        @Schema(description = "尺码")
        private String size;

        //其他属性
        @Schema(description = "样式描述")
        private String desc;
    }

    @Schema(description = "商品描述")
    @Data
    public static class ProductDesc {

        @Schema(description = "设计灵感")
        private String designInspiration;

        private String babyInformation;
    }
}
