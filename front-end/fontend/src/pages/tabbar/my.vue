<script setup>
    const imageSrc = ref('')

    const chooseImage = () => {
        uni.chooseImage({
                count: 1, //最多可以选择的图片张数，默认9
                sizeType: ['compressed'], //original 原图，compressed 压缩图，默认二者都有
                sourceType: ['album'], //album 从相册选图，camera 使用相机，默认二者都有。如需直接开相机或直接选相册，请只使用一个选项
                success: (res) => { //成功返回的函数
                    console.log('图片路径为：', res.tempFilePaths[0]) //选着的图片
                    var imageSrc = res.tempFilePaths[0] //将图片的地址赋值给imageSrc
                    uni.uploadFile({ //上传图片
                        url: '@/detail_3/detail_3.vue', //开发者服务器 url
                        filePath: imageSrc, //要上传文件资源的路径。
                        fileType: 'image', //文件类型，image/video/audio
                        name: 'data', //文件对应的 key , 开发者在服务器端通过这个 key 可以获取到文件二进制内容
                        success: (res) => { //接口调用成功的回调函数
                            console.log('uploadImage success, res is:', res)
                            uni.showToast({ //消息提示框
                                title: '上传成功',
                                icon: 'success',
                                duration: 1000
                            }),
                            uni.setStorage({
                                key:'image1',
                                data:imageSrc
                            })
                            this.imageSrc = imageSrc
                        },
                        fail: (err) => { //接口调用失败的回调函数	
                            console.log('失败返回：', err);
                            uni.showModal({ //消息提示框
                                content: err.errMsg, //错误信息
                                showCancel: false
                            });
                        }
                    });
                },
                fail: (err) => { //图片接口调用失败的回调函数	
                    console.log('chooseImage fail', err)

                // 判断是否允许调用摄像头
                uni.getSetting({
                    success: (res) => {
                        let authStatus = res.authSetting['scope.album'];
                        if (!authStatus) {
                            uni.showModal({
                                title: '授权失败',
                                content: 'Hello uni-app需要从您的相册获取图片，请在设置界面打开相关权限',
                                success: (res) => {
                                if (res.confirm) {
                                    uni.openSetting()
                                }
                        }
                    })
                        }
                    }
                }) 
                }
            })
    }

    const userinfo = ref('1')

    const orderStatus = [
        {
            id: 1,
            url: 'waitPay.svg',
            name: '待付款'
        },
        {
            id: 2,
            url: 'receiv.svg',
            name: '待收货'
        },
        {
            id: 3,
            url: 'evaluat.svg',
            name: '待评价'
        },
        {
            id: 4,
            url: 'allOrder.svg',
            name: '全部订单'
        }
    ]

    const onClickStatus = (index) => {
        uni.navigateTo({
            url: `/pagesA/pages/my/order?index=${index}`
        })
    }

    const useCenter = [
        {
            id: 1,
            icon: 'message.svg',
            title: '消息中心'
        },
        {
            id: 2,
            icon: 'hb.svg',
            title: '汉币中心'
        },
        {
            id: 3,
            icon: 'evaluate.svg',
            title: '我的评价'
        },
        {
            id: 4,
            icon: 'tw.svg',
            title: '我的图文'
        },
        {
            id: 5,
            icon: 'address.svg',
            title: '收货地址'
        }
    ]

    const onClickFunction = (item) => {
        console.log(item, 'item');
        const { id } = item
        const obj = {
            '1': 'message',
            '2': 'moneny',
            '3': 'reviewed',
            '4': 'tw',
        }

        if(id === 5) {
            uni.navigateTo({
                url: `/pagesA/pages/address/receiving`
            })
            return
        }

        const val = obj[id]
        if(!val) return

        uni.navigateTo({
            url: `/pagesA/pages/my/${val}`
        })
    }


    const JumpEdit = () => {
        uni.navigateTo({
            url: '/pagesA/pages/my/dataEditing'
        })
    }


    const onLogout = () => {
        uni.showModal({
            title: '提示',
            content: '确定退出吗',
            success: function (res) {
                if (res.confirm) {
                    console.log('用户点击确定');
                } else if (res.cancel) {
                    console.log('用户点击取消');
                }
            }
        });
    }

    onUnload(() => {
        // 页面卸载时
        imageSrc.value = '';
    })
</script>

<template>
    <view class="w-100vw bg-#ccc">
        <view class="bg">
            <template v-if="imageSrc">
                <!-- 如果纯在图片 -->
                <image :src="imageSrc" class="image" mode="widthFix"></image>
            </template>
            <template v-else>
                <!-- 没有原始图片 -->
                <view class="uni-hello-addfile" @click="chooseImage">+</view>
            </template>
        </view>
        <view class="bg-#fff px-3 pb-3">
            <view class="content_botton">
                <view class="content_info" v-if="userinfo">
                    <view class="flex h-16">
                        <image src="https://ts2.cn.mm.bing.net/th?id=OIP-C.qcssiqIxJl_5KTHne8ntWAAAAA&w=250&h=250&c=8&rs=1&qlt=90&o=6&pid=3.1&rm=2" mode="widthFix"></image>
                        <view class="w-90% flex justify-end items-center gap-15">
                            <view class="font-600">昵称</view>
                            <view 
                                class="border_xy py-2 px-3 rd-2 color-#FF75A3"
                                @click="JumpEdit"
                            >资料编辑</view>
                        </view>
                    </view>
                </view>
                <view v-else class="noLogin">
                    <view class="flex h-16">
                        <image src="https://ts4.cn.mm.bing.net/th?id=OIP-C.X-VG5gTN2ak8rGRij3oCogAAAA&w=212&h=212&c=8&rs=1&qlt=90&o=6&pid=3.1&rm=2" mode="widthFix"></image>
                        <view class="w-90% flex justify-end items-center gap-15">
                            <view class="font-600">请点击登录</view>
                        </view>
                    </view>
                </view>
                <!--  -->
                <view class="my_order">
                    <view 
                        class="my_order_item" 
                        v-for="(item, index) in orderStatus" 
                        :key="item.id"
                        @click="onClickStatus(index)"
                    >
                        <view class="my_order_content">
                            <image :src="'/static/tabbar/' + item.url" mode="widthFix"></image>
                            <view class="color-#666 font-600">{{item.name}}</view>
                        </view>
                    </view>
                </view>
            </view>
        </view>
        <view class="my-3 bg-#fff p-3">
            <template
                v-for="item of useCenter"
                :key="item.id"
            >
                <view class="flex items-center" @click="onClickFunction(item)">
                    <image 
                        :src="'/static/tabbar/' + item.icon" 
                        mode="widthFix"
                        class="w-8 h-8"
                    >
                    </image>
                    <view 
                        :class="[
                            'h-11', 'ml-3', 'flex-1', 
                            'flex', 'justify-between', 
                            'items-center', 
                            'border_b'
                            ]"
                        >
                        <text class="color-#666">{{ item.title }}</text>
                        <u-icon 
                        name="arrow-right" 
                        color="#FC7DA6" 
                        size="28"
                        ></u-icon>
                    </view>
                </view>
            </template>
            <view class="flex items-center" @click="onLogout">
                    <image 
                        src="/static/tabbar/logout.svg" 
                        mode="widthFix"
                        class="w-8 h-8"
                    >
                    </image>
                    <view 
                        :class="[
                            'h-11', 'ml-3', 'flex-1', 
                            'flex', 'justify-between', 
                            'items-center'
                            ]"
                        >
                        <text class="color-#666">退出登录</text>
                    </view>
                </view>
        </view>
        
        <view class="bg-#fff p-3">
            <view class="flex items-center gap-5">
                <image 
                src="/static/tabbar/sz.jpg" 
                mode="widthFix"
                class="w-30 h-20"
                >
                </image>
                <view class="flex flex-col gap-3">
                    <text class="font-600">绾青丝汉服社</text>
                    <text>电话: 12312341234</text>
                </view>
            </view>
        </view>
    </view>
</template>
     
<style lang="scss" scoped>
    .image {
        width: 100%;
    }
    
    .tit {
        font-size: 40px;
        display: flex;
        justify-content: center;
        background-color: antiquewhite;
    }
    
    .bg {
        padding: 20px 12px;
    }
    
    .uni-hello-addfile {
        height: 200rpx;
        text-align: center;
        font-size: 40px;
        display: flex;
        align-items: center;
        justify-content: center;
        border: 1px solid #fff;
    }
    
    .uni-hello-addfile:active {
        background-color: aqua;
    }

    .content_botton{
	   .noLogin{
            position: relative;
            left: 45%;
            transform: translate(-50%);
            height: 120rpx;
            background-color:#fff;
		   image{
            position: absolute;
            left: 10%;
            top: -25%;
            width: 140rpx;
            height: 140rpx;
            border-radius: 70rpx;
		   }
	   }
	   .content_info{
            position: relative;
            height: 120rpx;
            background-color:#fff;
		   image{
            position: absolute;
            left: 10%;
            top: -25%;
            width: 140rpx;
            height: 140rpx;
            border-radius: 70rpx;
		   }
	   }
	   .my_order{
		   margin-top: 10rpx;
		   border-radius: 20rpx;
           display: flex;
           justify-content: space-around;
		   .my_order_title{
			   border-radius: 20rpx;
			   padding-left: 20rpx;
			   line-height: 80rpx;
			   height: 80rpx;
		   }
		   .my_order_item{
			  .my_order_content{
				  display: flex;
				  flex-direction: column;
				  align-items: center;
				  justify-content: space-between;
                  gap: 5px;
				  image{
				  		width: 80rpx;
				  		height: 50rpx;
				  }
			  }
		   }
	   }
   }

   .border_xy {
    border: 2px solid #FF75A3;
   }

   .border_b {
    border-bottom: 1px solid #ccc;
   }
</style>

