import service from '@/utils/request'

// 登录
export function loginApi (data) {
    return service({
        url: 'user/auth/login/wechat',
        method: 'post',
        data
    })
}
