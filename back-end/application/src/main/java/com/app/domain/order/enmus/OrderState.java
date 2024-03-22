package com.app.domain.order.enmus;

import com.baomidou.mybatisplus.annotation.IEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;


/**
 * 订单状态
 * @author xxl
 * @since 2024/3/20
 */
@Getter
@Schema(description = "订单状态")
public enum OrderState implements IEnum<String> {
    /**
     * 下单
     * 表示用户创建订单的操作。
     */
    @Schema(description = "下单")
    PLACE_ORDER(POrderState.PENDING_PAYMENT,COrderState.PENDING_PAYMENT),

    /**
     * 付款
     * 表示用户完成订单支付的操作。
     */
    @Schema(description = "付款")
    MAKE_PAYMENT(POrderState.PAID,COrderState.PENDING_SHIPMENT),

    /**
     * 发货
     * 表示卖家发出商品的操作。
     */
    @Schema(description = "发货")
    SHIP_ORDER(POrderState.PENDING_RECEIPT,COrderState.SHIPPED),

    /**
     * 确认收货
     * 表示买家确认收到商品的操作。
     */
    @Schema(description = "确认收货")
    CONFIRM_RECEIPT(POrderState.RECEIVED,COrderState.COMPLETED),

    /**
     * 关闭订单
     * 表示订单因各种原因被关闭，不再进行后续操作。
     */
    @Schema(description = "关闭订单")
    CLOSE_ORDER(POrderState.CLOSED,COrderState.CLOSED),

    /**
     * 申请退款
     * 表示买家因为某些原因申请退款的操作。
     */
    @Schema(description = "申请退款")
    APPLY_FOR_REFUND(POrderState.PENDING_REFUND,COrderState.PENDING_REFUND),

    /**
     * 退款
     * 表示卖家或系统完成退款操作。
     */
    @Schema(description = "退款")
    REFUND(POrderState.REFUNDED,COrderState.REFUNDED);

    @Schema(description = "pc端订单状态")
    private final POrderState pOrderState;
    @Schema(description = "c端订单状态")
    private final COrderState cOrderState;

    OrderState(POrderState pOrderState, COrderState cOrderState) {
        this.pOrderState = pOrderState;
        this.cOrderState = cOrderState;
    }


    @Override
    public String getValue() {
        return name();
    }

    /**
     * pc端订单状态
     */
    @Schema(description = "pc端订单状态")
    public enum POrderState {
        /**
         * 待付款
         * 表示订单已生成，但买家尚未进行付款。
         */
        @Schema(description = "待付款")
        PENDING_PAYMENT,

        /**
         * 已付款
         * 表示买家已成功完成订单支付。
         */
        @Schema(description = "已付款")
        PAID,

        /**
         * 待收货
         * 表示卖家已发货，买家正在等待收货。
         */
        @Schema(description = "待收货")
        PENDING_RECEIPT,

        /**
         * 已收货
         * 表示买家已确认收货，订单交易接近完成。
         */
        @Schema(description = "已收货")
        RECEIVED,

        /**
         * 已关闭
         * 表示订单因某种原因被关闭，无法继续进行交易过程。
         */
        @Schema(description = "已关闭")
        CLOSED,

        /**
         * 待退款
         * 表示买家申请退款，等待卖家处理。
         */
        @Schema(description = "待退款")
        PENDING_REFUND,

        /**
         * 已退款
         * 表示卖家已处理退款，资金返还给买家。
         */
        @Schema(description = "已退款")
        REFUNDED
    }


    /**
     *  client端订单状态
     */
    @Schema(description = "client端订单状态")
    public enum COrderState {
        /**
         * 待付款
         * 订单已创建，等待买家付款。
         */
        @Schema(description = "待付款")
        PENDING_PAYMENT,

        /**
         * 待发货
         * 买家已付款，等待卖家发货。
         */
        @Schema(description = "待发货")
        PENDING_SHIPMENT,

        /**
         * 待收货
         * 卖家已发货，买家等待收货。
         */
        @Schema(description = "待收货")
        PENDING_RECEIPT,

        /**
         * 已发货
         * 卖家已将订单商品发出，商品在途中。
         */
        @Schema(description = "已发货")
        SHIPPED,

        /**
         * 已完成
         * 交易成功完成，买家确认收货。
         */
        @Schema(description = "已完成")
        COMPLETED,

        /**
         * 已关闭
         * 订单因各种原因被关闭，交易未成功完成。
         */
        @Schema(description = "已关闭")
        CLOSED,

        /**
         * 待退款
         * 买家申请退款，等待卖家处理。
         */
        @Schema(description = "待退款")
        PENDING_REFUND,

        /**
         * 已退款
         * 卖家已同意退款，交易资金已返还给买家。
         */
        @Schema(description = "已退款")
        REFUNDED
    }
}
