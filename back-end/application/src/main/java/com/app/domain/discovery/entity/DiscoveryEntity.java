package com.app.domain.discovery.entity;

import com.app.domain.base.Entity;
import com.app.domain.user.entity.UserEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 商品朋友圈(SysProductDiscovery)表实体类
 *
 * @author makejava
 * @since 2024-03-21 15:42:05
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "商品发现")
@TableName(value = "sys_discovery",autoResultMap = true)
public class DiscoveryEntity extends Entity {

    @Serial
    private static final long serialVersionUID = 1320708788200717542L;

    //用户id
    @Schema(description = "用户id")
    @JsonView(IGNORE.class)
    private String userId;

    //文本描述
    @Schema(description = "文本描述")
    @JsonView(INSERT.class)
    private String descText;

    //图片路径JSON
    @Schema(description = "图片路径")
    @TableField(typeHandler = JacksonTypeHandler.class)
    @JsonView(INSERT.class)
    private List<String> img;

    //点赞数量
    @Schema(description = "点赞数量")
    @JsonView(UPDATE.class)
    private Integer likes;

    //点赞用户 JSON
    @Schema(description = "点赞用户")
    @JsonView(IGNORE.class)
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> likeUsers;

    @TableField(typeHandler = JacksonTypeHandler.class)
    @JsonView(IGNORE.class)
    @Schema(description = "未读赞的信息列表，这个数组的size表示多少未读赞")
    private List<String> unreadLikes;

    @Schema(description = "评论信息")
    @JsonView(IGNORE.class)
    @TableField(exist = false)
    private List<DiscoveryCommentEntity> comments;

    @Schema(description = "点赞用户信息")
    @JsonView(IGNORE.class)
    @TableField(exist = false)
    private List<UserEntity> unreadLikeInfos;

    @Schema(description = "用户信息")
    @JsonView(IGNORE.class)
    @TableField(exist = false)
    private UserEntity user;

    public List<String> getLikeUsers() {
        return Objects.isNull(likeUsers) ? new ArrayList<>() : likeUsers;
    }

    public List<String> getUnreadLikes() {
        return Objects.isNull(unreadLikes) ? new ArrayList<>() : unreadLikes;
    }
}

