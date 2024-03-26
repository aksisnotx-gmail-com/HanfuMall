package com.app.domain.discovery.enums;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author xxl
 * @since 2024/3/26
 */
@Schema(description = "已读类型")
public enum ReadType {
    /**
     * 点赞类型
     */
    LIKE,

    /**
     * 评论类型/回复类型
     */
    COMMENT_OR_REPLY
}
