import service from '@/utils/request'

export function getSwiperListApi () {
    return service({
        url: 'product/banner/query',
        method: 'get'
    })
}

/**
 * @description 根据分类获取商品列表
 */
export function getProductByTypeApi (type) {
    return service({
        url: `product/detail/type?type=${type}`,
        method: 'get'
    })
}

/**
 * @description 获取所有商品
 */
export function getAllProductApi (current) {
    return service({
        url: `product/detail/all?current=${current}`,
        method: 'get'
    })
}

/**
 * @description 根据商品名字搜索商品
 */
export function getProductBySearchApi (productName) {
    return service({
        url: `product/detail/search?productName=${productName}`,
        method: 'get'
    })
}

/**
 * @description 根据标题ID获取商品
 */
export function getProductByIdApi (productId) {
    return service({
        url: `product/detail/${productId}`,
        method: 'get'
    })
}
