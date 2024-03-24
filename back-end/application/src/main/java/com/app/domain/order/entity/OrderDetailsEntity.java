package com.app.domain.order.entity;

import java.io.Serial;
import java.util.Date;

import com.app.domain.base.Entity;
import com.app.domain.product.entity.ProductSkuEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 订单详情(SysOrderDetails)表实体类
 *
 * @author makejava
 * @since 2024-03-20 22:37:07
 */

@TableName(value = "sys_order_details",autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "订单详情")
public class OrderDetailsEntity extends Entity {

    @Serial
    private static final long serialVersionUID = 5289564186190120895L;

    //商品sku_Id
    @Schema(description = "商品sku信息，防止删除/修改出现问题商品,解决处在订单中的商品需要被冻结问题")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private ProductSkuEntity productSku;

    //订单ID
    @Schema(description = "订单ID")
    private String orderId;

    //是否评价(1 已评价 0 未评价)
    @Schema(description = "是否评价(1 已评价 0 未评价)")
    private Integer isEvaluate;

    //商品ID
    @Schema(description = "商品ID")
    private String productId;
}

