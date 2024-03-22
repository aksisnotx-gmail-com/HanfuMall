package com.app.domain.discovery.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author xxl
 * @since 2024/3/22
 */
@Schema(description = "评论类型")
public enum CommentType implements IEnum<String> {
    /**
     * 评论
     */
    @Schema(description = "评论")
    COMMENT,
    /**
     * 回复
     */
    @Schema(description = "回复")
    REPLY
    ;

    @Override
    public String getValue() {
        return name();
    }
}
