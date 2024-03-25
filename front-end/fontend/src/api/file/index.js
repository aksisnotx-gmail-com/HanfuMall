import service from '@/utils/request'

/**
 * @description 获取图片
 * @param {string} fileId 
 */
export function getImgApi (fileId) {
    return service({
        url: `file/download/${fileId}`,
        method: 'get'
    })
}

/**
 * @description 上传图片
 */
export function uploadImgApi (data) {
    return service({
        url: `file/upload`,
        method: 'post',
        data
    })
}