<script setup>
    import { useUserStore } from '@/store/modules/user'
    import { loginApi } from '@/api/auth'


    const userStore = useUserStore()

    const { userInfo } = storeToRefs(userStore)

    const onChooseAvatar = async (e) => {
       userInfo.value.avatar = e.detail.avatarUrl
        
        uni.setStorage({
            key: 'avatar',
            data: e.detail.avatarUrl
        });
    }

    const getPhoneNumber = (e) => {
        wx.login({
            success: (res) => {
                userInfo.value.code = res.code
            } 
        })
        // 先获取access_token
        wx.request({
            url: `https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=${userInfo.value.appId}&secret=${userInfo.value.secret}`,
            method: "GET",
            success: (res) => {
                wx.request({
                    url: `https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=${res.data.access_token}`,
                    method: "POST",
                    header: {
                        'Content-type': 'application/json'
                    },
                    data: {
                        code: e.detail.code
                    },
                    success: (res) => {
                        const phone = res.data.phone_info.phoneNumber
                        userInfo.value.phoneNumber = phone
                        uni.setStorageSync('phoneNumber', phone);
                    }
                })
            }
        })
    }

    const login = async () => {
        const objParam = {
            avatar: userInfo.value.avatar,
            nickname: userInfo.value.nickname,
            code: userInfo.value.code,
            phoneNumber: userInfo.value.phoneNumber
        }
        const res = checkParams(objParam)
        if(res) {
            uni.setStorageSync('LoginShow', true)

            const res = await loginApi({
                ...objParam,
                "gender": 0
            })
            const { token } = res.data
            uni.setStorageSync('token', token)
            uni.showToast({
                title: '登录成功',
                icon: 'success',
                mask: true
            })

            setTimeout(() => {
                uni.switchTab({
                    url: '/pages/tabbar/my'
                })
            }, 200)
        }
    }

    const getNickName = (e) => {
        const val = e.detail.value
        userInfo.value.nickname = val
        uni.setStorageSync('nickname', val)
    }

    function checkParams(params) {
        // 属性名与提示信息的映射
        const fields = {
            avatar: '头像为空',
            nickname: '昵称为空',
            code: '验证码为空',
            phoneNumber: '电话号码为空'
        };

        // 遍历映射并检查每个属性
        for (const [key, message] of Object.entries(fields)) {
            if (!params[key]) {
                uni.showToast({
                    title: message,
                    icon: 'error',
                    mask: true
                })
                return false
            }
        }
        
        return true;
    }
</script>

<template>
        <view 
            class="page-container"
        >                
            <image
                v-if="userInfo.avatar" 
                :src="userInfo.avatar" 
                mode="widthFix"
                class="avatar_img"
            >
            </image>
            <view v-else class="border_xy">
                暂无头像
            </view>
            <input
                @change="getNickName"
                :value="userInfo.nickname"
                type="nickname" 
                placeholder="请输入昵称" 
                name="nickname" 
                class="nickname"
            />
            <button 
                @chooseavatar="onChooseAvatar" 
                open-type="chooseAvatar" 
                type="primary"
                class="text-3 m-0"
            >设置头像</button>
            <button 
                open-type="getPhoneNumber" 
                @getphonenumber="getPhoneNumber"
                type="primary"
                class="text-3 m-0"
            >获取手机号</button>
            <button 
                @click="login" 
                type="primary"
                class="text-3 m-0"
            >登录</button>
        </view>
</template>

<style scoped>
    .page-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    min-height: 100vh;
    padding: 20px;
    background-color: #f7f7f7;
    font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
    }

    /* 头像样式 */
    .avatar_img {
        width: 120px;
        height: 120px;
        margin-bottom: 30px;
        border-radius: 50%;
        object-fit: cover;
    }

    .nickname {
        width: 280px;
        padding: 10px 15px;
        margin-bottom: 20px;
        border: 1px solid #ddd;
        border-radius: 4px;
        background-color: #fff;
        font-size: 16px;
        color: #666;
    }

    /* 按钮基础样式 */
    button {
        width: 280px;
        padding: 12px 0;
        margin-bottom: 10px;
        border: none;
        border-radius: 4px;
        font-size: 16px;
        cursor: pointer;
        transition: background-color 0.3s, color 0.3s;
    }

    .border_xy {
        width: 120px;
        height: 120px;
        display: flex;
        justify-content: center;
        align-items: center;
        margin-bottom: 30px;
        border: 1px solid #ddd;
        border-radius: 50%;
        color: #666;
        font-size: 18px;
    }
</style>