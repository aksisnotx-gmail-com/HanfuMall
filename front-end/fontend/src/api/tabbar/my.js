import service from '@/utils/request'

// 订单
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

// 汉币中心
/**
 * @description 查询汉币余额
 */
export function getWalletApi () {
    return service({
        url: `user/wallet/query`,
        method: 'get'
    })
}

/**
 * @description 充值
 */
export function rechargeWalletApi (decimal) {
    return service({
        url: `user/wallet/recharge?decimal=${decimal}`,
        method: 'get'
    })
}

/**
 * @description 获取我的评价
 */
export function getMyEvaluateApi (current) {
    return service({
        url: `product/comment/getMyEvaluate?current=${current}`,
        method: 'get'
    })
}

/**
 * @description 删除评价
 */
export function delMyEvaluateApi (commentId, orderId) {
    return service({
        url: `product/comment/delete/?commentId=${commentId}&orderId=${orderId}`,
        method: 'get'
    })
}

/**
 * @description 消息中心
 */
export function getMsgApi () {
    return service({
        url: `discovery/getMsg`,
        method: 'get'
    })
}

/**
 * @description 已读信息
 */
export function readMsgApi (data) {
    return service({
        url: `discovery/read`,
        method: 'post',
        data
    })
}

/**
 * @description 根据类型获取发现-评论
 */
export function getByTypeApi (type, id) {
    return service({
        url: `discovery/getByType?type=${type}&id=${id}`,
        method: 'get'
    })
}