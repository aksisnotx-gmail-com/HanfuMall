package com.app.domain.order.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author xxl
 * @since 2024/3/23
 */
@Data
@Schema(description = "订单参数")
@Valid
public class OrderParam implements Serializable {

    @Serial
    private static final long serialVersionUID = -8902417331375595283L;

    @Schema(description = "收货地址")
    @NotBlank(message = "收货地址不能为空")
    private String deliveryAddress;

    @Schema(description = "商品")
    @NotEmpty(message = "商品不能为空")
    @Valid
    private List<OrderDetailsParam> productSkuIds;

    @Data
    @Schema(description = "订单详情参数")
    @Valid
    public static class OrderDetailsParam implements Serializable {

        @Serial
        private static final long serialVersionUID = -9191345976887799704L;

        @Schema(description = "商品id")
        @NotBlank(message = "商品id不能为空")
        private String skuId;

        @Schema(description = "商品数量")
        @Min(value = 1, message = "商品数量最小为1")
        private Integer number;

        @Schema(description = "总价格")
        @NotBlank(message = "总价格不能为空")
        private BigDecimal totalPrice;
    }
}
