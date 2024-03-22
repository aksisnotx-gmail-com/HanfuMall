package com.app.domain.user.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author xxl
 * @since 2024/3/19
 */
@Schema(description = "角色")
public enum Role implements IEnum<String> {
    /**
     * 管理员
     */
    @Schema(description = "管理员")
    ADMIN,

    /**
     * 买家
     */
    @Schema(description = "买家")
    BUYER;

    @Override
    public String getValue() {
        return name();
    }
}
