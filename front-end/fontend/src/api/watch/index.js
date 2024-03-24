import service from '@/utils/request'

/**
 * @description 查询所有的发现
 */
export function getAllDiscoveryApi (current) {
    return service({
        url: `discovery/getAll/discovery?current=${current}`,
        method: 'get'
    })
}

/**
 * @description 点赞
 */
export function LikeApi (discoveryId) {
    return service({
        url: `discovery/like/${discoveryId}`,
        method: 'get'
    })
}


/**
 * @description 取消点赞
 */
export function cancelLikeApi (discoveryId) {
    return service({
        url: `discovery/cancelLike/${discoveryId}`,
        method: 'get'
    })
}

/**
 * @description 查询所有的评论
 */
export function getAllCommentApi (discoveryId) {
    return service({
        url: `discovery/getAll/comment/${discoveryId}`,
        method: 'get'
    })
}
