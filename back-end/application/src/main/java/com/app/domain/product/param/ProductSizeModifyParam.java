package com.app.domain.product.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author xxl
 * @since 2024/3/20
 */
@Data
@Schema(description = "商品尺码修改参数")
public class ProductSizeModifyParam implements Serializable {

    @Serial
    private static final long serialVersionUID = -2739999304891254061L;

    @Schema(description = "商品id")
    @NotBlank(message = "商品id不能为空")
    private String productId;

    @Schema(description = "尺码id,添加尺码信息不用带ID,修改要带")
    private String id;

    //价格
    @Schema(description = "价格")
    private Double price;

    //库存
    @Schema(description = "库存")
    private Integer stock;

    //尺码
    @Schema(description = "尺码")
    @NotBlank(message = "尺码不能为空")
    private String size;

    //颜色
    @Schema(description = "样式")
    private ProductDetailParam.ProductStyle style;

    //推荐
    @Schema(description = "是否推荐，1 是 0 不是")
    private Integer isRecommend;

    //特惠价格
    @Schema(description = "特惠价格")
    private Double specialPrice;

    //是否促销
    @Schema(description = "是否特惠，1 是 0 不是")
    private Integer isSpecial;
}
