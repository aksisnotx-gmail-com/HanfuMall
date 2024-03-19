package com.app.domain.product.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author xxl
 * @since 2024/3/19
 */
@Schema(description = "发货类型")
public enum ShippingType implements IEnum<String> {

    @Schema(description = "快递")
    EXPRESS,

    @Schema(description = "自提")
    PICK_UP;

    @Override
    public String getValue() {
        return name();
    }
}
