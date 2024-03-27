const ORDER_STATE = {
    PLACE_ORDER:{front:'待付款',back:'待付款'},
    MAKE_PAYMENT:{front:'已付款',back:'待发货'},
    SHIP_ORDER:{front:'待收货',back:'待收货'},
    CONFIRM_RECEIPT:{front:'已完成',back:'已完成'},
    CLOSE_ORDER:{front:'已关闭',back:'已关闭'},
    APPLY_FOR_REFUND:{front:'待退款',back:'待退款'},
    REFUND:{front:'已退款',back:'已退款'},
    DELETE_ORDER:{front:'已删除',back:'已删除'},
}