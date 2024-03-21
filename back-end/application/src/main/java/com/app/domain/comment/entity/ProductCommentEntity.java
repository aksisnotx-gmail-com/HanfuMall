package com.app.domain.comment.entity;

import cn.hutool.json.JSONUtil;
import com.app.domain.base.Entity;
import com.app.domain.user.entity.UserEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.List;

/**
 * 商品评论(SysProductComment)表实体类
 *
 * @author makejava
 * @since 2024-03-20 21:39:08
 */

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "商品评论")
@TableName("sys_product_comment")
public class ProductCommentEntity extends Entity {

    @Serial
    private static final long serialVersionUID = 4779993855062212763L;

    //星级
    @Schema(description = "星级")
    @JsonView(INSERT.class)
    @Max(value = 5, message = "星级必须在0-5之间",groups = INSERT.class)
    @Min(value = 0, message = "星级必须在0-5之间",groups = INSERT.class)
    private Integer starRating;

    //用户ID
    @Schema(description = "用户ID")
    @JsonView(IGNORE.class)
    private String userId;

    //评论内容
    @Schema(description = "评论内容")
    @JsonView(INSERT.class)
    @NotBlank(message = "评论内容不能为空",groups = INSERT.class)
    private String commentContent;

    //评论图片json形式为多张图片
    @Schema(description = "评论图片json形式为多张图片")
    @JsonView(IGNORE.class)
    private String commentImgUrl;

    //商品ID
    @Schema(description = "商品ID")
    @NotBlank(message = "商品ID不能为空",groups = INSERT.class)
    @JsonView(INSERT.class)
    private String productId;

    @TableField(exist = false)
    @Schema(description = "评论图片json形式为多张图片")
    @JsonView(INSERT.class)
    private List<String> commentImgUrlList;

    @TableField(exist = false)
    @Schema(description = "用户信息")
    @JsonView(IGNORE.class)
    private UserEntity user;

    @JsonView(IGNORE.class)
    public void setImgUrlListToStr(List<String> urlList) {
        this.commentImgUrl = JSONUtil.toJsonStr(urlList);
    }

    @JsonView(IGNORE.class)
    public void setImgUrlStrToList(String urlStr) {
        this.commentImgUrlList = JSONUtil.toList(urlStr,String.class);
    }
}

