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
    @Schema(description = "获取所有的发现不包括审核未通过")
    ALL,
    /**
     * 获取自己的
     */
    @Schema(description = "获取自己的")
    MY,
    /**
     * 未审核通过的发现
     */
    DISCOVERY_UN_PASS,
    /**
     * 所有的发现包括未审核通过
     */
    ALL_DISCOVERY
}
