package com.app.domain.discovery.entity;

import com.app.domain.base.Entity;
import com.app.domain.discovery.enums.CommentType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 评论内容(SysDiscoveryComment)表实体类
 *
 * @author makejava
 * @since 2024-03-21 16:32:58
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_discovery_comment")
public class DiscoveryCommentEntity extends Entity {

    @Serial
    private static final long serialVersionUID = 1161755475088750167L;

    //商品发现ID/评论ID
    @Schema(description = "商品发现ID/评论ID")
    @JsonView(INSERT.class)
    @NotBlank(message = "商品发现ID/评论ID不能为空",groups = INSERT.class)
    private String commentId;

    //评论内容
    @Schema(description = "评论内容")
    @NotBlank(message = "评论内容不能为空",groups = INSERT.class)
    @JsonView(INSERT.class)
    private String content;

    //评论人ID
    @Schema(description = "评论人ID")
    @JsonView(IGNORE.class)
    private String userId;

    //是否已读(1 已读 0 未读)
    @Schema(description = "是否已读(1 已读 0 未读)")
    @JsonView(IGNORE.class)
    private Integer isRead;

    @Schema(description = "评论类型")
    @JsonView(INSERT.class)
    private CommentType commentType;
}

