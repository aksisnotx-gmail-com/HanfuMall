package com.app.domain.user.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * @author xxl
 * @since 2024/3/19
 */
public enum Role implements IEnum<String> {
    /**
     * 商家
     */
    MERCHANT,

    /**
     * 买家
     */
    BUYER;

    @Override
    public String getValue() {
        return name();
    }
}
