package com.app.domain.product.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * @author xxl
 * @since 2024/3/19
 */
@Schema(description = "商品类型")
public enum ProductType implements IEnum<String> {

    @Schema(description = "汉服")
    HAN_FU,

    @Schema(description = "汉元素")
    HAN_YUAN_SU,

    @Schema(description = "配饰周边")
    ACCESSORIES
    ;

    @Override
    public String getValue() {
        return name();
    }
}
