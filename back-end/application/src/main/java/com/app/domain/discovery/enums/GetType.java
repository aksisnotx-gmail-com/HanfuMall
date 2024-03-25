package com.app.domain.discovery.enums;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author xxl
 * @since 2024/3/25
 */
@Schema(description = "获取类型")
public enum GetType {
    /**
     * 获取所有的
     */
    @Schema(description = "获取所有的")
    ALL,
    /**
     * 获取自己的
     */
    @Schema(description = "获取自己的")
    MY
}
