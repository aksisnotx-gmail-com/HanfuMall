<script setup>
    import { getAllOrderApi } from '@/api/tabbar/my'
    import { orderCloseApi, orderPayApi, orderReceiveApi, orderApplyRefundApi, delOrderApi } from '@/api/tabbar/order'
    import { JumpDetail } from '../utils/index'
    
    const pageInfo = reactive({
        current: 1,
        size: 20,
        total: 0
    })

    const orderList = ref([])
    const getAllOrder = async (currentt = 1) => {
        const res = await getAllOrderApi(currentt)
        const { records, total, size, current } = res.data
        pageInfo.total = total
        pageInfo.size = size
        pageInfo.current = current
        orderList.value = [ ...orderList.value, ...records ]
    }

    const closeOrder = async (orderId) => {
        uni.showModal({
            title: '提示',
            content: '确定要关闭订单吗？',
            success: async (res) => {
                if(res.confirm) {
                    const response = await orderCloseApi(orderId)
                    if(response.data) {
                        uni.showToast({
                            title: '关闭成功',
                            icon: 'success',
                            mask: true,
                            duration: 1000
                        })
                        getAllOrder(pageInfo.current)
                    }
                }
            }
        })

    }

    const onPay = (orderId) => {
        uni.showModal({
            title: '提示',
            content: '确定要付款吗？',
            success: async (res) => {
                if(res.confirm) {
                    const response = await orderPayApi(orderId)
                    if(response.data) {
                        uni.showToast({
                            title: '付款成功',
                            icon: 'success',
                            mask: true,
                            duration: 3000
                        })
                        getAllOrder(pageInfo.current)
                    }
                }
            }
        })

    }

    const onReceive = async (orderId) => {
        uni.showModal({
            title: '提示',
            content: '确定要确认收货吗？',
            success: async (res) => {
                if(res.confirm) {
                    const response = await orderReceiveApi(orderId)
                    if(response.data) {
                        uni.showToast({
                            title: '确认收货成功',
                            icon: 'success',
                            mask: true, 
                            duration: 2000
                        })
                        getAllOrder(pageInfo.current)
                    }
                }
            }
        })
    }

    const applyRefundOrder = async (orderId) => {
        uni.showModal({
            title: '提示',
            content: '确定退款吗?',
            success: async (res) => {
                if(res.confirm) {
                    const response = await orderApplyRefundApi(orderId)
                    if(response.data) {
                        uni.showToast({
                            title: '申请成功, 请等待~',
                            icon: 'success',
                            mask: true, 
                            duration: 2000
                        })
                        getAllOrder(pageInfo.current)
                    }
                }
            }
        })
    }

    const delOrder = async (orderId) => {
        uni.showModal({
            title: '提示',
            content: '确定要删除订单吗？',
            success: async (res) => {
                if(res.confirm) {
                    const response = await delOrderApi(orderId)
                    if(response.data) {
                        uni.showToast({
                            title: '删除成功',
                            icon: 'success',
                            mask: true, 
                            duration: 2000
                        })
                        getWaitReceive(pageInfo.current)
                    }
                }
            }
        })
    }
    
    const onEvaluate = (orderId) => {
        uni.navigateTo({
            url: `/pagesA/pages/my/evaluate?orderid=${orderId}`
        })
    }
    
    onMounted(() => {
        getAllOrder()
    })
    
    onReachBottom(async () => {		
		uni.showLoading({
            title: '加载中'
        });
		const currentTotal = pageInfo.current * pageInfo.size
		if(currentTotal < pageInfo.total) {
            pageInfo.current++
            await getAllOrder(pageInfo.current)
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
		await getAllOrder()
		uni.stopPullDownRefresh()
		uni.hideLoading()
	})

    const mapOrderState = (state) => {
        const ORDER_STATE = {
            'PLACE_ORDER':{front:'待付款',back:'待付款'},
            'MAKE_PAYMENT':{front:'已付款',back:'发货'},
            'SHIP_ORDER':{front:'待收货',back:'已发货'},
            'CONFIRM_RECEIPT':{front:'已完成',back:'已完成'},
            'CLOSE_ORDER':{front:'已关闭',back:'已关闭'},
            'APPLY_FOR_REFUND':{front:'待退款',back:'退款'},
            'REFUND':{front:'已退款',back:'已退款'},
            'DELETE_ORDER':{front:'已删除',back:'已删除'},
        }

        return ORDER_STATE[state] ? ORDER_STATE[state].front : ''
    }
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
                    <view class="flex gap-4">
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
                        <view 
                            class="flex gap-3"
                            @click="JumpDetail(item.id)"
                        >
                            <text class="text-3.5">共{{ item.skuNumber }}件商品, 买家实付</text>
                            <text class="color-#DC143C font-600">¥ {{ item.totalPrice }}</text>
                        </view>
                        <template v-if="item.state == 'PLACE_ORDER'">
                            <view class="text-3 flex gap-5">
                                <text 
                                    class="border_xy py-1 px-1.5" 
                                    @click="closeOrder(item.id)"
                                >关闭订单</text>
                                <text 
                                    class="b-1 b-solid b-#FF679A rd-4.5 py-1 px-1.5 color-#FF679A"
                                    @click="onPay(item.id)"
                                >立即付款</text>
                            </view>
                        </template>
                        <template v-else-if="item.state == 'MAKE_PAYMENT'">
                            <view class="text-3 flex gap-5">
                                <text class="border_xy py-1 px-1.5"
                                    @click="applyRefundOrder(item.id)"
                                >申请退款</text>
                                <template v-if="item.state == 'MAKE_PAYMENT'">
                                    <text 
                                        class="b-1 b-solid b-#FF679A rd-4.5 py-1 px-1.5 color-#FF679A"
                                    >待发货</text>
                                </template>
                                <template v-else>
                                    <text 
                                        class="b-1 b-solid b-#FF679A rd-4.5 py-1 px-1.5 color-#FF679A"
                                        @click="onReceive(item.id)"
                                    >确认收货</text>
                                </template>
                            </view>
                        </template>
                        <template v-else-if="item.state == 'SHIP_ORDER'">
                            <view class="text-3 flex gap-5">
                                <text 
                                    class="b-1 b-solid b-#FF679A rd-4.5 py-1 px-1.5 color-#FF679A"
                                    @click="onEvaluate(item.id)"
                                    >评价</text>
                            </view>
                        </template>
                        <template v-else>
                            <view class="text-3 flex gap-5">
                                <text 
                                    class="border_xy py-1 px-1.5" 
                                    @click="delOrder(item.id)"
                                >删除订单</text>
                                <text class="b-1 b-solid b-#FF679A rd-4.5 py-1 px-1.5 color-#FF679A">{{ mapOrderState(item.state) }}</text>
                            </view>
                        </template>
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