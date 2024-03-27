package com.app.domain.order.entity;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import cn.hutool.core.util.IdUtil;
import com.app.domain.base.Entity;
import com.app.domain.order.enmus.OrderState;
import com.app.domain.product.entity.ProductDetailsEntity;
import com.app.domain.product.entity.ProductSkuEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 订单列表(SysOrder)表实体类
 *
 * @author makejava
 * @since 2024-03-20 22:45:03
 */

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "订单信息")
@TableName(value = "sys_order",autoResultMap = true)
public class OrderEntity extends Entity {

    public static final Integer UN_EVALUATE = 0;

    public static final Integer EVALUATE = 1;

    public static final Integer UN_HANDLER = -1;

    @Serial
    private static final long serialVersionUID = 6509027597663570020L;

    //订单状态
    @Schema(description = "订单状态")
    private OrderState state;

    //下单编号
    @Schema(description = "下单编号")
    private String orderNumber;

    @Schema(description = "用户ID")
    private String userId;

    @Schema(description = "收货地址")
    private String deliveryAddress;

    //商品sku
    @Schema(description = "商品sku信息，防止删除/修改出现问题商品,解决处在订单中的商品需要被冻结问题")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private ProductSkuEntity productSku;

    @Schema(description = "商品详情信息，防止删除/修改出现问题商品,解决处在订单中的商品需要被冻结问题")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private ProductDetailsEntity productDetail;

    //是否评价(1 已评价 0 未评价 -1 未购买)
    @Schema(description = "是否评价(1 已评价 0 未评价 -1 表示未处理商品评价)")
    private Integer isEvaluate;

    @Schema(description = "购买数量")
    private Integer skuNumber;

    @Schema(description = "总价")
    private BigDecimal totalPrice;

    @Schema(description = "尺码")
    private String size;

    public static OrderEntity create(OrderState state,
                                     String userId,
                                     String deliveryAddress,
                                     ProductSkuEntity productSku,
                                     ProductDetailsEntity detailsEntity,
                                     Integer skuNumber,
                                     BigDecimal totalPrice,
                                     String size
                                     ){
        OrderEntity order = new OrderEntity();
        order.setProductSku(productSku);
        order.setProductDetail(detailsEntity);
        order.setIsEvaluate(UN_HANDLER);
        order.setSkuNumber(skuNumber);
        order.setTotalPrice(totalPrice);
        order.setState(state);
        order.setOrderNumber("" + System.currentTimeMillis());
        order.setUserId(userId);
        order.setDeliveryAddress(deliveryAddress);
        order.setSize(size);
        return order;
    }
}

