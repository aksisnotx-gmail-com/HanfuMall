package com.app.domain.order.controller;

import com.app.controller.Controller;
import com.app.domain.order.entity.OrderEntity;
import com.app.domain.order.param.OrderParam;
import com.app.domain.user.entity.LoginUser;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sdk.resp.RespEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author xxl
 * @since 2024/3/23
 */
@Tag(name = "购物车 - 订单")
@RequestMapping("/shoppingCart/order")
@RestController
@Validated
public class OrderController extends Controller {

    //下单
    @PostMapping("/create")
    @Operation(summary = "创建订单")
    public RespEntity<Boolean> createOrder(OrderParam orderParam) {
        return RespEntity.success(orderService.createOrder(orderParam,LoginUser.getLoginUserId()));
    }

    //关闭订单
    @GetMapping("/close/{orderId}")
    @Operation(summary = "关闭订单")
    public RespEntity<Boolean> closeOrder(@PathVariable String orderId) {
        return RespEntity.success(orderService.closeOrder(orderId,LoginUser.getLoginUser()));
    }

    //付款
    @GetMapping("/pay/{orderId}")
    @Operation(summary = "付款")
    public RespEntity<Boolean> payOrder(@PathVariable String orderId) {
        return RespEntity.success(orderService.payOrder(orderId,LoginUser.getLoginUser()));
    }

    //申请退款
    @GetMapping("/applyRefund/{orderId}")
    @Operation(summary = "申请退款")
    public RespEntity<Boolean> applyRefundOrder(@PathVariable String orderId) {
        return RespEntity.success(orderService.applyRefundOrder(orderId,LoginUser.getLoginUser()));
    }

    //退款
    @GetMapping("/refund/{orderId}")
    @Operation(summary = "退款")
    public RespEntity<Boolean> refundOrder(@PathVariable String orderId) {
        return RespEntity.success(orderService.refundOrder(orderId,LoginUser.getLoginUser()));
    }

    //发货
    @GetMapping("/send/{orderId}")
    @Operation(summary = "发货")
    public RespEntity<Boolean> sendOrder(@PathVariable String orderId) {
        return RespEntity.success(orderService.sendOrder(orderId,LoginUser.getLoginUser()));
    }

    //确认收货
    @GetMapping("/receive/{orderId}")
    @Operation(summary = "确认收货")
    public RespEntity<Boolean> receiveOrder(@PathVariable String orderId) {
        return RespEntity.success(orderService.receiveOrder(orderId,LoginUser.getLoginUser()));
    }

    //删除订单
    @GetMapping("/delete/{orderId}")
    @Operation(summary = "删除订单")
    public RespEntity<Boolean> deleteOrder(@PathVariable String orderId) {
        return RespEntity.success(orderService.deleteOrder(orderId,LoginUser.getLoginUser()));
    }

    //获取订单
    @GetMapping("/get")
    @Operation(summary = "获取订单[前台获取自己的,后台获取所有]")
    public RespEntity<Page<OrderEntity>> getOrder() {
        return RespEntity.success(orderService.getOrderByUser(LoginUser.getLoginUser()));
    }

    //获取订单详情
    @GetMapping("/get/{orderId}")
    @Operation(summary = "获取订单详情")
    public RespEntity<OrderEntity> getOrder(@PathVariable String orderId) {
        return RespEntity.success(orderService.getOrder(orderId));
    }

    //待付款
    @GetMapping("/getWaitPay")
    @Operation(summary = "待付款")
    public RespEntity<Page<OrderEntity>> getWaitPay() {
        return RespEntity.success(orderService.getWaitPay(LoginUser.getLoginUserId()));
    }

    //待收货
    @GetMapping("/getWaitReceive")
    @Operation(summary = "待收货")
    public RespEntity<Page<OrderEntity>> getWaitReceive() {
        return RespEntity.success(orderService.getWaitReceive(LoginUser.getLoginUserId()));
    }

    //待评价
    @GetMapping("/getWaitEvaluate")
    @Operation(summary = "待评价")
    public RespEntity<Page<OrderEntity>> getWaitEvaluate() {
        return RespEntity.success(orderService.getWaitEvaluate(LoginUser.getLoginUserId()));
    }
}
