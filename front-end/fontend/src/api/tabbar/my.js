import service from '@/utils/request'

/**
 * @description 获取自己所有订单
 */
export function getAllOrderApi (current) {
    return service({
        url: `shoppingCart/order/get?current=${current}`,
        method: 'get'
    })
}

/**
 * @description 获取待付款的订单
 */
export function getWaitPayApi (current) {
    return service({
        url: `shoppingCart/order/getWaitPay?current=${current}`,
        method: 'get'
    })
}

/**
 * @description 获取待收货的订单
 */
export function getWaitReceiveApi (current) {
    return service({
        url: `shoppingCart/order/getWaitReceive?current=${current}`,
        method: 'get'
    })
}

/**
 * @description 获取待评价的订单
 */
export function getWaitEvaluateApi (current) {
    return service({
        url: `shoppingCart/order/getWaitEvaluate?current=${current}`,
        method: 'get'
    })
}

