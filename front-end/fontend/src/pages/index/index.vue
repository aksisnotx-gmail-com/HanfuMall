<script setup>
  import { onLoad } from "@dcloudio/uni-app";

  const userForm = reactive({
    account: "",
    code: ""
  })

  onLoad((options) => {
    console.log(options, 'opt');
  })
</script>

<template>
  <view class="content">
   
    <view class="v1">
  
  <!-- v2父容器  子view使用绝对布局 -->
    <view class="v2">
      <view class="dltext">登录</view>
      
      <!-- 手机号 -->
     <view class="phoneCs">
        <!-- <image src="/images/phone.png" class="ph"></image> -->
        <input v-model="userForm.account" placeholder="请输入账号" />
      </view>
    
      <!-- 密码 -->
      <view class="passwordCs">
        <!-- <image src="/images/password.png" class="ps"></image> -->
        <input v-model="userForm.code" type="password" placeholder="请输入密码" />
        <!-- <image src="/images/eye-open.png" class="eye"></image> -->
      </view>
     
      <!-- 注册 和忘记密码  -->
      <view class="zhuce">
        <navigator>立即注册</navigator>
        <navigator>忘记密码?</navigator>
      </view>
    
      <!-- 登录按钮 -->
      <view class="denglu">
        <button class="btn-dl" type="primary">登录</button>
      </view>
    
      <!-- 协议区  xieyi -->
      <view class="xieyi">
        <checkbox class="isXY"></checkbox>
        <view class="text-xy">同意小程序的《使用协议》</view>
      </view>
    </view>
    
    </view>
  </view>
</template>

<style>
/* 最大的父元素 */
.v1{
  display: block;
  position:absolute;
  height: 100%;
  width: 100%;
  background: #356363;
  /* 换图片 */
  
}
/* 白色区域 */
.v1 .v2{
  position: relative;
  margin-top: 150rpx;
  left: 100rpx; 
  width: 545rpx;
  height: 750rpx;
  background: #FFFFFF;
  border-radius: 50rpx;
}
/* 白色区域内的登录文本 */
.v1 .v2 .dltext{
  width: 100%;
  display: flex;
  justify-content: center;
  margin-top: 50rpx;
  position: absolute;
  height: 100rpx;
  font-size: 60rpx;
  font-family: Helvetica;
  color: #000000;
  line-height: 100rpx;
  letter-spacing: 2rpx;
}
/* 手机图片+输入框+下划线的父容器view */
.v1 .v2 .phoneCs{
  margin-top: 200rpx;
  margin-left: 35rpx;
  position: absolute;
  display: flex;
  width:450rpx ;
  height: 80rpx ;
  border-bottom: 3rpx solid rgb(58, 57, 57);
}
/* 手机图标 */
.v1 .v2 .phoneCs .ph{
  margin-top: 5rpx;
  margin-left: 30rpx;
  width: 55rpx;
  height: 55rpx;
}
/* 手机号输入框 */
.v1 .v2 .phoneCs input{
  width: 200rpx;
  font-size: 25rpx ;
  margin-top: 15rpx;
  margin-left: 30rpx;
}
/* 密码图标+输入框+小眼睛图标+下划线父容器view */
.v1 .v2 .passwordCs{
  margin-top: 300rpx;
  margin-left: 35rpx;
  position: absolute;
  display: flex;
  width:450rpx ;
  height: 80rpx ;
  border-bottom: 3rpx solid rgb(58, 57, 57);
}
/* 密码图标 */
.v1 .v2 .passwordCs .ps{
  margin-top: 5rpx;
  margin-left: 30rpx;
  width: 55rpx;
  height: 55rpx;
}
/* 眼睛 图标*/
.v1 .v2 .passwordCs .eye{
  margin-top: 5rpx;
  margin-left: 65rpx;
  width: 55rpx;
  height: 55rpx;
}
/* 密码输入框 */
.v1 .v2 .passwordCs input{
  width: 200rpx;
  font-size: 25rpx ;
  margin-top: 15rpx;
  margin-left: 30rpx;
}
/* 注册+忘记密码父容器 */
.v1 .v2 .zhuce{
  font-size: 25rpx;
  margin-left: 85rpx;
  width: 370rpx;
  margin-top: 450rpx;
  position: absolute;
  display: flex;
  justify-content: space-between;
}
/* 登录按钮容器view */
.v1 .v2 .denglu{
  width: 350rpx;
  height: 50rpx;
  position: absolute;
  margin-top: 500rpx;
  margin-left: 85rpx;
  
}
/* 登录按钮 */
.v1 .v2 .denglu button{
  padding: 0rpx;
  line-height: 50rpx;
  font-size: 25rpx;
  width: 100%;
  height: 100%;
  border-radius: 30rpx;
}
/* 复选框+协议文字容器view */
.v1 .v2 .xieyi{
  margin-left: 10rpx;
  margin-top: 580rpx;
  width: 400rpx;
  display: flex;
  position: absolute;
}
/* 复选框 */
.v1 .v2 .xieyi .isXY{
  margin-left: 85rpx;
  /* 修改复选框的大小 */
  transform:scale(.6);
}
/* 协议文本 */
.v1 .v2 .xieyi .text-xy{
  color: #000000;
  font-size: 18rpx;
  display: flex;
  justify-content: center;
  align-items: center;
}
/* 提示文本 */
.v3{
  padding-left: 100rpx;
  display: flex;
  position: relative;
  width: 100%;
  height: 60rpx;
  padding-top: 50rpx;
}
.v3 .line{
  margin-left: 5rpx;
  margin-top: 18rpx;
  width: 120rpx;
  height: 3rpx;
 background-color:#FFFFFF;
}
.v3 .lText{
  color: #FFFFFF;
  font-size: 25rpx;
}
/* qq wx wb */
.qwwIcon{
  margin-top: 100rpx;
  padding-top: 50rpx;
  position: relative;
  display: flex;
  width: 100%;
  height: 150rpx;
}
.qwwIcon image{
  padding-left: 120rpx;
  width: 80rpx;
  height: 80rpx;
}
</style>
