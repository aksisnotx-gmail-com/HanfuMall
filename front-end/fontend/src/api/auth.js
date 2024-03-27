import service from '@/utils/request'

// 登录
export function loginApi (data) {
    return service({
        url: 'user/auth/login/wechat',
        method: 'post',
        data
    })
}

// 获取个人信息
export function getUserApi () {
    return service({
        url: 'user/get',
        method: 'get'
    })
}

// 修改信息
export function updateApi (data) {
    return service({
        url: 'user/modifyUserInfo',
        method: 'post',
        data
    })
}
