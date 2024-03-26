package com.app.domain.comment.vo;

import com.app.domain.comment.entity.ProductCommentEntity;
import com.app.domain.order.entity.OrderEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author xxl
 * @since 2024/3/26
 */
@Data
@Schema(description = "评价")
public class CommentVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -8668238922720604323L;

    @Schema(description = "评价")
    private OrderEntity order;

    @Schema(description = "评价")
    private ProductCommentEntity comment;
}
