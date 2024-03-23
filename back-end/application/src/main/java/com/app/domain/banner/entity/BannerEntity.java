package com.app.domain.banner.entity;

import java.io.Serial;

import com.app.domain.base.Entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

/**
 * 轮播图(SysBanner)表实体类
 *
 * @author makejava
 * @since 2024-03-23 17:29:10
 */
@EqualsAndHashCode(callSuper = true)
@Schema(description = "轮播图")
@Data
@TableName(value = "sys_banner",autoResultMap = true)
public class BannerEntity extends Entity {

    @Serial
    private static final long serialVersionUID = -7958951853677651893L;

    //轮播图
    @Schema(description = "轮播图")
    @TableField(typeHandler = JacksonTypeHandler.class)
    @JsonView(INSERT.class)
    private String bannerUrl;
}

