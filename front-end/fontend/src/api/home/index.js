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
export function getProductByTypeApi (typeId, current) {
    return service({
        url: `product/detail/type?typeId=${typeId}&current=${current}`,
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


/**
 * @description 获取推荐商品
 */
export function getRecommendProductsApi () {
    return service({
        url: `product/recommendProducts`,
        method: 'get'
    })
}

/**
 * @description 获取限时特惠商品
 */
export function getSpecialProductsApi () {
    return service({
        url: `product/specialProducts`,
        method: 'get'
    })
}

/**
 * @description 获取所有商品类型
 */
export function getProductTypeApi () {
    return service({
        url: `product/type`,
        method: 'get'
    })
}