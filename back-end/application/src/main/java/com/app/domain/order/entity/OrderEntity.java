package com.app.domain.order.entity;

import java.io.Serial;
import java.util.Date;

import com.app.domain.base.Entity;
import com.app.domain.order.enmus.OrderState;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 订单列表(SysOrder)表实体类
 *
 * @author makejava
 * @since 2024-03-20 22:45:03
 */

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "订单信息")
@TableName("sys_order")
public class OrderEntity extends Entity {

    @Serial
    private static final long serialVersionUID = 6509027597663570020L;

    //订单状态
    @Schema(description = "订单状态")
    private OrderState state;

    //下单编号
    @Schema(description = "下单编号")
    private Date orderNumber;

    @Schema(description = "用户ID")
    private String userId;
}

