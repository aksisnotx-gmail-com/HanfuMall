package com.app.domain.shippingcart.entity;

import java.io.Serial;
import java.util.Date;

import com.app.domain.base.Entity;
import com.app.domain.product.entity.ProductDetailsEntity;
import com.app.domain.product.param.vo.ProductVO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Map;

/**
 * 购物车(SysShoppingCart)表实体类
 *
 * @author makejava
 * @since 2024-03-22 10:54:44
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_shopping_cart")
@Schema(description = "购物车")
public class ShoppingCartEntity extends Entity {

    @Serial
    private static final long serialVersionUID = -1888746983435211863L;

    //商品id
    @Schema(description = "商品id")
    @JsonView({INSERT.class})
    @NotBlank(message = "商品id不能为空", groups = {INSERT.class})
    private String productSkuId;

    //数量
    @Schema(description = "数量")
    @JsonView({INSERT.class})
    private Integer number;

    //用户ID
    @Schema(description = "用户ID")
    @JsonView({IGNORE.class})
    @NotBlank(message = "用户ID不能为空", groups = {INSERT.class})
    private String userId;

    @JsonView({IGNORE.class})
    @TableField(exist = false)
    private Map<String,Object> productMap;
}

