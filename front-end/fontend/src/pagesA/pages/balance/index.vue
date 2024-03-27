<script setup>
    import { useCarStore } from '@/store/modules/car'
    import { useAddressStore } from '@/store/modules/address'
    const carStore = useCarStore()

    const JumpAddress = () => {
        uni.navigateTo({
            url: '/pagesA/pages/address/receiving'
        })
    }


    const submitOrder = () => {
        if(!Object.keys(address.value)?.length) {
            uni.showToast({
                title: '请选择地址',
                icon: 'none',
                duration: 2000
            })
            return
        }

        // TODO 提交订单的接口 考虑汉币的数量
        uni.showToast({
            title: '支付成功',
            icon: 'success',
            duration: 2000
        })
    }

    const addressStore = useAddressStore()
    const address = ref({})
    onShow(() => {
        address.value = { ...addressStore.curAddress }
    }) 
</script>

<template>
    <view class="h-100vh bg-#F4F4F4">
        <view class="bg-#fff rd-4 mt-3 relative" @click="JumpAddress">
            <template v-if="Object.keys(address)?.length">
                <view class="ml-6 py-3 px-2">
                    <view class="flex items-center gap-3">
                        <image
                            src="@/pagesA/static/address.svg"
                            mode="aspectFit"
                            class="w-7 h-7"
                        />
                        <view class="flex flex-col">
                            <view class="flex text-3.5 gap-3">
                                <text>{{ address.name }}</text>
                                <text class="color-#999">{{ address.phone }}</text>
                            </view>
                            <view>
                                <text class="text-3.5 color-#666">{{ address.address }}</text>
                            </view>
                        </view>
                    </view>
                    <view class="absolute right-0 top-26%">
                        <u-icon 
                        name="arrow-right" 
                        color="#FC7DA6" 
                        size="28"
                        ></u-icon>
                    </view>
                </view>
            </template>
            <template v-else>
                <view class="layout-abs-center py-3 px-2">
                    <view class="layout-items-center gap-3">
                        <u-icon 
                            name="edit-pen" 
                            color="#FC7DA6" 
                            size="28"
                        ></u-icon>
                        <text class="text-3 color-#666">添加收货地址</text>
                    </view>
                    <view class="absolute right-0 top-22.5%">
                        <u-icon 
                        name="arrow-right" 
                        color="#FC7DA6" 
                        size="28"
                        ></u-icon>
                    </view>
                </view>
            </template>
        </view>

        <view class="mt-3 px-3">
            <!-- <view class="bg-#fff flex gap-4 px-3 py-2 rd-4">
                <image
                    src="https://cdn.uviewui.com/uview/swiper/2.jpg"
                    mode="aspectFill"
                    class="img"
                />
                <view class="w-60 flex flex-col justify-between">
                    <text class="name">原创宋制汉服女重工刺绣长衫精子齐腰褶裙春夏款</text>
                    <view class="flex items-center gap-3 text-3 color-#666">
                        <text>暗纹褶子</text>
                        <text>s</text>
                        <text class="font-600">x1</text>
                    </view>
                    <text class="color-#DC143C font-600">¥ {{ carStore.totalPrice }}</text>
                </view>
            </view> -->
            <template v-for="item of carStore.goodsList" :key="item.id">
                <view class="bg-#fff flex gap-4 px-3 py-2 rd-4 mb-3">
                    <image
                        :src="item.img"
                        mode="aspectFill"
                        class="img"
                    />
                    <view class="w-60 flex flex-col justify-between">
                        <text class="name">{{ item.name }}</text>
                        <view class="flex items-center gap-3 text-3 color-#666">
                            <text>{{ item.size }}</text>
                            <text>s</text>
                            <text class="font-600">x{{ item.count }}</text>
                        </view>
                        <text class="color-#DC143C font-600">¥ {{ item.sumPrice }}</text>
                    </view>
                </view>
            </template>
        </view>

        <view class="tabbar">
            <view class="totalPrice">
                <text class="color-#ccc">实付款: </text>
                <text class="font-600">¥ {{ carStore.totalPrice }}元</text>
            </view>
            <view class="submitOrder" @click="submitOrder">
                结算
            </view>
		</view>
    </view>
</template>

<style scoped>
    .img {
        height: 100px;
        width: 136px;
        margin-left: 10rpx;
        border-radius: 8%;
    }

    .name {
        font-size: 25rpx;
        font-weight: 600;
        color: #3E3E3E;
        letter-spacing: 1.86rpx;
        /* white-space: nowrap;
        text-overflow: ellipsis;
        overflow: hidden; */
    }

    .tabbar {
		width: calc(100% - 24px);
        height: 60px;
		background-color: #fff;
		position: fixed;
		bottom: 30px;
        left: 12px;
        right: 12px;
		display: flex;
		align-items: center;
		justify-content: space-around;
		border-radius: 8% 8%;
    }

    .totalPrice {
        font-size: 28rpx;
        color: #3E3E3E;
        letter-spacing: 2.29rpx;
        color: #DC143C;
    }

    .submitOrder {
        width: 208rpx;
        height: 80rpx;
        background-color: #7DA1DC;
        border-radius: 14rpx;
        font-size: 36rpx;
        color: #FFFFFF;
        letter-spacing: 2.57rpx;
        display: flex;
        align-items: center;
        justify-content: center;
    }

    .list{
        width: 100%;
        height: 208rpx;
        background: #fff;
        box-shadow: 0 8rpx 16rpx 0 rgba(83,66,49,0.08);
        border-radius: 24rpx;
        display: flex;
        justify-content: space-around;
        align-items: center;
        margin-top: 10px;
    }

    .l {
        display: flex;
        align-items: center;
    }
</style>