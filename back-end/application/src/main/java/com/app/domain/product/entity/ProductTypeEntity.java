package com.app.domain.product.entity;

import java.io.Serial;
import java.util.Date;

import com.app.domain.base.Entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 商品分类类型(SysProductType)表实体类
 *
 * @author makejava
 * @since 2024-04-01 06:07:47
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "商品分类类型")
@TableName("sys_product_type")
public class ProductTypeEntity extends Entity {
    @Serial
    private static final long serialVersionUID = -9075760617571107495L;

    //类型
    @Schema(description = "类型")
    @JsonView({INSERT.class,UPDATE.class})
    @NotBlank(message = "类型不能为空", groups = {INSERT.class,UPDATE.class})
    private String type;

}

