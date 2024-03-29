<script setup>
    import { getWalletApi, rechargeWalletApi } from '@/api/tabbar/my'

    const curMoneny = ref(0)
    const getWallet = async () => {
        const res = await getWalletApi()
        const { message, code, data } = res
        if(code !== 200) {
            uni.showToast({
                icon: 'error',
                duration: 3000,
                title: message
            })
        } else {
            curMoneny.value = data.balance
        }
        
    }

    const rechargeShow = ref(false)

    const rechargeNum = ref(0)
    const onClose = () => {
        rechargeShow.value = false
    }

    const onRecharge = async (decimal) => {
        uni.showLoading({
            title: '充值中...',
            mask: true
        })

        const res = await rechargeWalletApi(decimal)
        if(res.data) {
            uni.showToast({
                icon: 'success',
                duration: 3000,
                title: '充值成功'
            })
            await getWallet()
            uni.hideLoading()
            rechargeShow.value = false
        }

    }
    const onAddMoneny = () => {
        if(!rechargeNum.value) {
            uni.showToast({
                icon: 'none',
                duration: 3000,
                title: '请输入充值金额'
            })
            return
        }


        if(typeof Number(rechargeNum.value) !== 'number') {
            uni.showToast({
                icon: 'none',
                duration: 3000,
                title: '请输入正确的金额'
            })
            return
        }

        onRecharge(rechargeNum.value)
    }

    const onJumpCar = () => {
        uni.switchTab({
            url: '/pages/tabbar/car'
        })
    }

    onShow(() => {
        getWallet()
    })
</script>

<template>
    <view class="h-100vh bg-#F2F2F2">
        <view class="bg-#fff">
            <view class="h-30 layout-abs-center font-600">
                <text class="color-#666">我的汉币: &nbsp;&nbsp;</text>
                <text class="color-#FF6699">{{ curMoneny }}</text>
            </view>
            <view class="flex items-center">
                <view class="flex-1 flex flex-col items-center gap-3">
                    <image
                        src="@/pagesA/static/recharge.svg"
                        mode="aspectFit"
                        class="w-15 h-15"
                    />
                    <text 
                        class="border_xy py-1 px-3 color-#666" 
                        @click="rechargeShow = true"
                    >去充值</text>
                </view>
                <view class="w-0.5 h-18 bg-#ccc"></view>
                <view class="flex-1 flex flex-col items-center gap-3">
                    <image
                        src="@/pagesA/static/car.svg"
                        mode="aspectFit"
                        class="w-15 h-15"
                    />
                    <text 
                        class="border_xy py-1 px-3 color-#666"
                        @click="onJumpCar"
                    >去购买</text>
                </view>
            </view>
            <u-divider text="" hairline></u-divider>
            <view class="h-30 flex flex-col py-3 px-3 gap-4">
                <text class="text-5 ">汉币规则</text>
                <text>汉币为本店铺通用货币, 可通过充值获得1rmb= 1汉币。</text>
            </view>
        </view>

        <u-popup 
            :show="rechargeShow"
            @close="onClose"
            :safeAreaInsetBottom="false"
        >
            <view class="h-40">
                <view class="py-3 px-10 flex justify-between">
                    <text class="color-#666" @click="onClose">取消</text>
                    <text class="color-#5987CB" @click="onAddMoneny">立即充值</text>
                </view>
                <view class="h-25 flex justify-center">
                    <view class="w-80 flex items-center justify-center">
                        <text>自定义充值价格: </text>
                        <view class="w-25">
                            <up-input
                            v-model="rechargeNum"
                            placeholder="请输入"
                            border="bottom"
                        ></up-input>
                        </view>
                    </view>
                </view>
            </view>
        </u-popup>
    </view>
</template>

<style scoped>
    .border_xy {
        border: 2px solid #ccc;
    }
</style>