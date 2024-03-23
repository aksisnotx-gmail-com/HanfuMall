package com.app.domain.order.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author xxl
 * @since 2024/3/23
 */
@Data
@Schema(description = "订单参数")
public class OrderParam implements Serializable {

    @Serial
    private static final long serialVersionUID = -8902417331375595283L;

    @Schema(description = "收货地址")
    @NotBlank(message = "收货地址不能为空")
    private String deliveryAddress;

    @Schema(description = "商品skuIds")
    @NotEmpty(message = "商品skuIds不能为空")
    private List<String> productSkuIds;
}
