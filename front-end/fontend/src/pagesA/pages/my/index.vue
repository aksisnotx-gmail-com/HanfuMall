<script setup>
import { WxLogin, editUserInfo } from '~/api/module/login.js'
import defaultImg from '../../assets/default.png'

const formSubmit = () => {
    console.log('form发生了submit事件，携带数据为：' + JSON.stringify(e.detail.value))
    var formdata = e.detail.value
    uni.showModal({
        content: '表单数据内容：' + JSON.stringify(formdata),
        showCancel: false
    });
}
const formReset = () => {
    console.log('清空数据')
}


const getInfo = () => {
    wx.login({
        async success (res) {
            if (res.code) {
                console.log('==>res.code', res.code);
                query.code = res.code
            } else {
                console.log('登录失败！' + res.errMsg)
            }
        }
    })
}
const onChooseAvatar = (e) => {
    query.avatar = e.detail.avatarUrl
    src.value = e.detail.avatarUrl
    console.log('==>src.value', src.value);
    uni.setStorage({
        key: 'avator',
        data: e.detail.avatarUrl
    });
}
async function getAll (e) {
    query.userName = e.detail.value.nickname
    uni.setStorage({
        key: 'username',
        data: e.detail.value.nickname
    });
    //发起网络请求
    await WxLogin(query)
    setTimeout(async () => {
        await editUserInfo(
            {
                phone: uni.getStorageSync('phone'),
                userImg: uni.getStorageSync('avator'),
                userName: uni.getStorageSync('username'),
                sex: 0
            }
        )
    }, 1000)
    uni.switchTab({
        url: `/pages/home/index`,
        animationType: 'pop-in',
        animationDuration: 300
    })
    uni.showToast({
        title: '登录成功',
        duration: 2000
    });

}
const src = ref(defaultImg)
const LoginShow = ref(true)
const login = () => {
    LoginShow.value = false
}


const query = reactive({
    appId: "wxec1cf291ff198934",
    avatar: '',
    code: "",
    secret: "868483dacf8c6b5a563863319760e2b6",
    userName: ''
})
const getPhoneNumber = (e) => {
    // 先获取access_token
    wx.request({
        url: 'https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wxb39792f7a5bfff5c&secret=cf307af2d1716bc6c29a40a01e630be3',
        method: "GET",
        success: (res) => {
            console.log(res, 'ress');
            console.log('==>res.data.access_token', res.data.access_token);
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
                    uni.setStorageSync('phone', res.data.phone_info.phoneNumber);
                }
            })
        }
    })
}

</script>

<template>
    <view v-if="LoginShow">
        <img src="../../assets/logo.png" alt=""
            style="display: flex;justify-content: center;align-items: center;width: 100%;margin-bottom: 15vh;">
        <view style="width: 80vw;margin: 0 auto;">
            <up-button @click="login" color="#3c9cff" type="primary" shape="circle" text="微信一键登录"></up-button>
        </view>

    </view>
    <view v-else>
        <form @submit="getAll">
            <view class="avatar">
                <up-avatar size="100" :src="src"></up-avatar>
            </view>
            <input style="margin-left: 10%;" :value="query.userName" type="nickname" placeholder="请输入昵称" name="nickname" />
            <view style="width: 80vw;margin: 0 auto;margin-top: 20px;">
                <button @click="getInfo" @chooseavatar="onChooseAvatar" open-type="chooseAvatar" type="primary"
                    name="nickname">设置头像</button>
            </view>
            <view style="width: 80vw;margin: 0 auto;margin-top: 20px;">
                <button open-type="getPhoneNumber" type="primary" @getphonenumber="getPhoneNumber">获取手机号</button>
            </view>
            <view style="width: 80vw;margin: 0 auto;margin-top: 20px;">
                <button form-type="submit" type="primary" shape="circle">登录</button>
            </view>
        </form>
    </view>
</template>

<style scoped lang="scss">
.avatar {
    display: flex;
    justify-content: center;
    align-items: center;
    margin-bottom: 10vh;
}
</style>