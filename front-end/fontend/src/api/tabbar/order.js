import service from '@/utils/request'

/**
 * @description 商品加入购物车
 */
export function createOrderApi (data) {
    return service({
        url: `shoppingCart/order/create`,
        method: 'post',
        data
    })
}
