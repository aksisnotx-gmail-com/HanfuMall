package com.app.domain.product.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author xxl
 * @since 2024/3/20
 */
@Data
@Schema(description = "商品详情修改参数")
public class ProductDetailModifyParam implements Serializable {

    @Serial
    private static final long serialVersionUID = -7913613563753504676L;

    //商品Id
    @Schema(description = "id")
    @NotBlank(message = "id不能为空")
    private String id;

    //发货地址
    @Schema(description = "发货地址")
    @NotBlank(message = "发货地址不能为空")
    private String deliveryAddress;

    //轮播图来自于sku json形式 List<String>
    @Schema(description = "轮播图URL")
    @NotEmpty(message = "轮播图不能为空")
    private List<String> carousel;

    //商品名称
    @NotBlank(message = "商品名称不能为空")
    @Schema(description = "商品名称")
    private String productName;

    //商品描述,图片都是为json
    @Schema(description = "商品描述")
    @NotEmpty(message = "商品描述不能为空")
    private List<String> descUrls;

    //商品类型：汉服、首饰
    @Schema(description = "商品类型ID")
    @NotEmpty(message = "商品类型不能为空")
    private List<String> productTypeIds;
}
