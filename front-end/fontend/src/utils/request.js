import { getToken,removeToken } from './auth';
import env from './config';

function service(options = {}) {
	options.url = `${env.baseUrl}${options.url}`;
	// 判断本地是否存在token，如果存在则带上请求头
	if (getToken()) {
		options.header = {
			'content-type': 'application/json',
			'token': `${getToken()}`	// 这里是token(可自行修改)
		};
	}

	return new Promise((resolved, rejected) => {
		options.success = (res) => {
			// 如果请求回来的状态码不是200则执行以下操作
			if (res.data.code !== 200) {
				// 非成功状态码弹窗
				uni.showToast({
					icon: 'none',
					duration: 3000,
					title: `${res.data.message}`
				});

				// 登陆失效
				if (res.data.code === 403) {
					// 清除本地token等信息
					uni.clearStorageSync()
					// 关闭所有页面返回到登录页
					uni.reLaunch({
						url: '/pages/tabbar/my'
					})
				}

				// 返回错误信息
				rejected(res)
			} else {
				// 请求回来的状态码为200则返回内容
				resolved(res.data)
			}
		};
		options.fail = (err) => {
			// 请求失败弹窗
			uni.showToast({
				icon: 'none',
				duration: 3000,
				title: '服务器错误,请稍后再试'
			});
			// 关闭所有页面返回到登录页
			uni.reLaunch({
				url: '/pages/tabbar/my'
			})
			rejected(err);
		};
		uni.request(options);
	});
}

export default service;
