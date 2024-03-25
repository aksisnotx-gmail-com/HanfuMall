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
 * @description 添加发现
 */
export function addDiscoveryApi (data) {
    return service({
        url: `discovery/discovery/publish`,
        method: 'post',
        data
    })
}

/**
 * @description 删除发现
 */
export function delDiscoveryApi (discoveryId) {
    return service({
        url: `discovery/delete/${discoveryId}`,
        method: 'get'
    })
}


/**
 * @description 点赞
 */
export function likeOrCancelApi (discoveryId) {
    return service({
        url: `discovery/likeOrCancel/${discoveryId}`,
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

/**
 * @description 评论 - 回复
 */
export function commentApi (data) {
    return service({
        url: `discovery/comment/publish`,
        method: 'post',
        data
    })
}

/**
 * @description 删除评论
 */
export function delCommentApi (commentId) {
    return service({
        url: `discovery/delete/comment/${commentId}`,
        method: 'get'
    })
}