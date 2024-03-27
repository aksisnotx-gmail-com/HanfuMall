import service from '@/utils/request'

/**
 * @description 商品加入购物车
 */
export function addCarApi (data) {
    return service({
        url: `shoppingCart/add`,
        method: 'post',
        data
    })
}

/**
 * @description 加-减购物车商品
 */
export function addOrReduceApi (itemId, number) {
    return service({
        url: `shoppingCart/addOrReduce?itemId=${itemId}&number=${number}`,
        method: 'get'
    })
}

/**
 * @description 查询购物车商品
 */
export function getAllApi (current) {
    return service({
        url: `shoppingCart/getAll?current=${current}`,
        method: 'get'
    })
}

