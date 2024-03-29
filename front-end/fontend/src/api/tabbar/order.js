import service from '@/utils/request'

/**
 * @description 生成订单
 */
export function createOrderApi (data) {
    return service({
        url: `shoppingCart/order/create`,
        method: 'post',
        data
    })
}

/**
 * @description 支付
 */
export function orderPayApi (orderId) {
    return service({
        url: `shoppingCart/order/pay/${orderId}`,
        method: 'get'
    })
}

/**
 * @description 关闭订单
 */
export function orderCloseApi (orderId) {
    return service({
        url: `shoppingCart/order/close/${orderId}`,
        method: 'get'
    })
}

/**
 * @description 关闭订单
 */
export function orderReceiveApi (orderId) {
    return service({
        url: `shoppingCart/order/receive/${orderId}`,
        method: 'get'
    })
}

/**
 * @description 申请退款
 */
export function orderApplyRefundApi (orderId) {
    return service({
        url: `shoppingCart/order/applyRefund/${orderId}`,
        method: 'get'
    })
}

/**
 * @description 获取订单详情
 */
export function orderDetailApi (orderId) {
    return service({
        url: `shoppingCart/order/get/${orderId}`,
        method: 'get'
    })
}

/**
 * @description 删除订单
 */
export function delOrderApi (orderId) {
    return service({
        url: `shoppingCart/order/delete/${orderId}`,
        method: 'get'
    })
}

/**
 * @description 评价
 */
export function addComment(data) {
    return service({
        url: `product/comment/publish`,
        method: 'post',
        data
    })
}

/**
 * @description 评价
 */ 
export function getCommentById (productId) {
    return service({
        url: `product/comment/queryAll/${productId}`,
        method: 'get'
    })
}