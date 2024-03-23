package com.app.domain.order.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xxl
 * @since 2024/3/23
 */
@Tag(name = "购物车 - 订单")
@RequestMapping("/shoppingCart/order")
@RestController
@Validated
public class OrderController {

    //下单
    //关闭订单
    //付款
    //申请退款
    //退款
    //发货
    //确认收货
    //获取订单
    //获取所有订单
    //删除订单
}
