<script setup>
    import { getWaitEvaluateApi } from '@/api/tabbar/my'
    import { JumpDetail } from '../utils/index'
    
    const pageInfo = reactive({
        current: 1,
        size: 20,
        total: 0
    })

    const orderList = ref([])
    const getWaitEvaluate = async (currentt = 1) => {
        const res = await getWaitEvaluateApi(currentt)
        const { records, total, size, current } = res.data
        pageInfo.total = total
        pageInfo.size = size
        pageInfo.current = current
        orderList.value = [ ...records ]
    }

    onMounted(() => {
        getWaitEvaluate()
    })

    const onEvaluate = (orderId) => {
        uni.navigateTo({
            url: `/pagesA/pages/my/evaluate?orderid=${orderId}`
        })
    }

    const mapOrderState = (state) => {
        const ORDER_STATE = {
            'PLACE_ORDER':{front:'待付款',back:'待付款'},
            'MAKE_PAYMENT':{front:'已付款',back:'待发货'},
            'SHIP_ORDER':{front:'待收货',back:'待收货'},
            'CONFIRM_RECEIPT':{front:'已完成',back:'已完成'},
            'CLOSE_ORDER':{front:'已关闭',back:'已关闭'},
            'APPLY_FOR_REFUND':{front:'待退款',back:'待退款'},
            'REFUND':{front:'已退款',back:'已退款'},
            'DELETE_ORDER':{front:'已删除',back:'已删除'},
        }

        return ORDER_STATE[state] ? ORDER_STATE[state].front : ''
    }

    onReachBottom(async () => {		
		uni.showLoading({
            title: '加载中'
        });
		const currentTotal = pageInfo.current * pageInfo.size
		if(currentTotal < pageInfo.total) {
            pageInfo.current++
            await getWaitEvaluate(pageInfo.current)
            uni.hideLoading()
        } else {
            uni.hideLoading()
            uni.showToast({
                title: '没有更多了',
                icon: 'error',
                mask: true,
                duration: 1000
            })
        }
	})

    onPullDownRefresh(async () => {
        const len = orderList.value.length
		orderList.value.splice(0, len)

		uni.showLoading({
            title: '加载中'
        });

		await getWaitEvaluate()

		uni.stopPullDownRefresh()
		uni.hideLoading()
	})
</script>

<template>
    <view class="px-2">
        <template v-if="!orderList.length">
            <view class="h-100vh layout-abs-center">
                <u-empty
                    mode="data"
                >
                </u-empty>
            </view>
        </template>

        <template v-else>
            <template v-for="item of orderList" :key="item.id">
                <view 
                    class="item_card"
                >
                    <view 
                        class="flex gap-4"
                        @click="JumpDetail(item.id)"
                    >
                        <image
                            :src="item.productSku.attribute.carouselUrl"
                            mode="aspectFill"
                            class="img"
                        />
                        <view class="w-60 flex flex-col justify-between">
                            <text class="name">{{ item.productDetail.productName }}</text>
                            <view class="flex items-center gap-3 text-3 color-#666">
                                <text>{{ item.productSku.attribute.desc }}</text>
                                <text>{{ item.size }}</text>
                                <text class="font-600">x {{ item.skuNumber }}</text>
                            </view>
                            <view class="layout-slide">
                                <text class="color-#DC143C font-600">¥ {{ item.totalPrice }}</text>
                                <text class="text-3.5 color-#FF679A">{{ mapOrderState(item.state) }}</text>
                            </view>
                        </view>
                    </view>
                    <view class="mt-3 pt-2 flex flex-col items-end gap-3 border_t">
                        <view class="flex gap-3">
                            <text class="text-3.5">共{{ item.skuNumber }}件商品, 买家实付</text>
                            <text class="color-#DC143C font-600">¥ {{ item.totalPrice }}</text>
                        </view>
                        <view class="text-3 flex gap-5">
                            <text 
                                class="b-1 b-solid b-#FF679A rd-4.5 py-1 px-1.5 color-#FF679A"
                                @click="onEvaluate(item.id)"
                            >评价</text>
                        </view>
                    </view>
                </view>
            </template>
        </template>
    </view>
</template>

<style scoped>
    .item_card {
        padding: 12px;
        border-radius: 15px;
        background: #fff;
        margin-bottom: 12px;
    }

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

    .border_xy {
        border: 1px solid #999;
        border-radius: 20px;
    }

    .border_t {
        border-top: 1px solid #ccc;
    }
</style>