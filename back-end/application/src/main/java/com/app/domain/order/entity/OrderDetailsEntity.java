package com.app.domain.order.entity;

import java.io.Serial;
import java.math.BigDecimal;

import com.app.domain.base.Entity;
import com.app.domain.product.entity.ProductDetailsEntity;
import com.app.domain.product.entity.ProductSkuEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;


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

    public static final Integer UN_EVALUATE = 0;

    public static final Integer EVALUATE = 1;

    public static final Integer UN_HANDLER = -1;

    @Serial
    private static final long serialVersionUID = 5289564186190120895L;

    //商品sku_Id
    @Schema(description = "商品sku信息，防止删除/修改出现问题商品,解决处在订单中的商品需要被冻结问题")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private ProductSkuEntity productSku;

    @Schema(description = "商品详情信息，防止删除/修改出现问题商品,解决处在订单中的商品需要被冻结问题")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private ProductDetailsEntity productDetail;

    //订单ID
    @Schema(description = "订单ID")
    private String orderId;

    //是否评价(1 已评价 0 未评价 -1 未购买)
    @Schema(description = "是否评价(1 已评价 0 未评价 -1 表示未处理商品评价)")
    private Integer isEvaluate;

    @Schema(description = "购买数量")
    private Integer skuNumber;

    @Schema(description = "总价")
    private BigDecimal totalPrice;
    public static OrderDetailsEntity create(ProductSkuEntity sku,ProductDetailsEntity productDetail,String orderId,Integer skuNumber){
        OrderDetailsEntity entity = new OrderDetailsEntity();
        entity.setProductSku(sku);
        entity.setProductDetail(productDetail);
        entity.setOrderId(orderId);
        entity.setIsEvaluate(UN_HANDLER);
        entity.setSkuNumber(skuNumber);
        return entity;
    }
}

