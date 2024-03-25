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
    public RespEntity<Boolean> refundOrder(@PathVariable String orderId) {
        return RespEntity.success(orderService.refundOrder(orderId,LoginUser.getLoginUserId()));
    }

    //退款
    @GetMapping("/refund/{orderId}")
    @Operation(summary = "退款")
    public RespEntity<Boolean> refundOrder(@PathVariable String orderId) {
        return RespEntity.success(orderService.refundOrder(orderId));
    }

    //发货
    @GetMapping("/send/{orderId}")
    @Operation(summary = "发货")
    public RespEntity<Boolean> sendOrder(@PathVariable String orderId) {
        return RespEntity.success(orderService.sendOrder(orderId));
    }

    //确认收货
    @GetMapping("/receive/{orderId}")
    @Operation(summary = "确认收货")
    public RespEntity<Boolean> receiveOrder(@PathVariable String orderId) {
        return RespEntity.success(orderService.receiveOrder(orderId,LoginUser.getLoginUserId()));
    }

    //删除订单
    @GetMapping("/delete/{orderId}")
    @Operation(summary = "删除订单")
    public RespEntity<Boolean> deleteOrder(@PathVariable String orderId) {
        return RespEntity.success(orderService.deleteOrder(orderId,LoginUser.getLoginUserId()));
    }

    //获取订单
    @GetMapping("/get")
    @Operation(summary = "获取订单")
    public RespEntity<Page<OrderEntity>> getOrder() {
        return RespEntity.success(orderService.getOrder(LoginUser.getLoginUserId()));
    }

    //获取所有订单
    @GetMapping("/getAll")
    @Operation(summary = "获取所有订单")
    public RespEntity<Page<OrderEntity>> getAllOrder() {
        return RespEntity.success(orderService.getAllOrder());
    }

    //获取订单详情
    @GetMapping("/get/{orderId}")
    @Operation(summary = "获取订单详情")
    public RespEntity<OrderEntity> getOrder(@PathVariable String orderId) {
        return RespEntity.success(orderService.getOrder(orderId));
    }

}
