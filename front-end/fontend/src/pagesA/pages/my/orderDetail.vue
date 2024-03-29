<script setup>
    import { orderDetailApi, orderCloseApi, orderPayApi, orderReceiveApi, orderApplyRefundApi, delOrderApi } from '@/api/tabbar/order'

    onLoad(options => {
        const { orderId } = options
        if(!orderId) return
        
        // 获取订单详情
        getOrderDetail(orderId)
    })

    const orderDetail = ref({})
    const addressInfo = ref({})
    const addressStr = ref('')
    const getOrderDetail = async (orderId) => {
        const res = await orderDetailApi(orderId)
        orderDetail.value = { ...res.data }

        if(!res.data.deliveryAddress.includes('name')) {
            addressStr.value = res.data.deliveryAddress
        } else {
            addressInfo.value = { ...JSON.parse(res.data.deliveryAddress) }
        }
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
                            duration: 1000,
                            success: () => {
                                setTimeout(() => {
                                    uni.navigateBack({
                                        delta: 1
                                    })
                                }, 1100)
                            }
                        })
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
                            duration: 1000,
                            success: () => {
                                setTimeout(() => {
                                    uni.navigateBack({
                                        delta: 1
                                    })
                                }, 1100)
                            }
                        })
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
                            duration: 1000,
                            success: () => {
                                setTimeout(() => {
                                    uni.navigateBack({
                                        delta: 1
                                    })
                                }, 1100)
                            }
                        })
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
                            duration: 1000,
                            success: () => {
                                setTimeout(() => {
                                    uni.navigateBack({
                                        delta: 1
                                    })
                                }, 1100)
                            }
                        })
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
                            duration: 1000,
                            success: () => {
                                setTimeout(() => {
                                    uni.navigateBack({
                                        delta: 1
                                    })
                                }, 1100)
                            }
                        })
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

    const intransitState = ref(['MAKE_PAYMENT', 'SHIP_ORDER'])
</script>

<template>
    <view class="h-100vh bg-#f4f4f4 px-2 pt-3">
        <view 
            class="item_card"
        >
            <view class="flex gap-4">
                <image
                    v-if="orderDetail.productSku"
                    :src="orderDetail.productSku.attribute.carouselUrl"
                    mode="aspectFill"
                    class="img"
                />
                <view class="w-60 flex flex-col justify-between">
                    <text class="name" v-if="orderDetail.productDetail">{{ orderDetail.productDetail.productName }}</text>
                    <view class="flex items-center gap-3 text-3 color-#666">
                        <text v-if="orderDetail.productSku">{{ orderDetail.productSku.attribute.desc }}</text>
                        <text>{{ orderDetail.size }}</text>
                        <text class="font-600">x {{ orderDetail.skuNumber }}</text>
                    </view>
                    <view class="layout-slide">
                        <text class="color-#DC143C font-600">¥ {{ orderDetail.totalPrice }}</text>
                        <text class="text-3.5 color-#FF679A">{{ mapOrderState(orderDetail.state) }}</text>
                    </view>
                </view>
            </view>
            <view class="mt-3 pt-2 flex flex-col items-end gap-3 border_t">
                <view class="flex gap-3">
                    <text class="text-3.5">共{{ orderDetail.skuNumber }}件商品, 买家实付</text>
                    <text class="color-#DC143C font-600">¥ {{ orderDetail.totalPrice }}</text>
                </view>
                <template v-if="orderDetail.state == 'PLACE_ORDER'">
                    <view class="text-3 flex gap-5">
                        <text 
                            class="border_xy py-1 px-1.5" 
                            @click="closeOrder(orderDetail.id)"
                        >关闭订单</text>
                        <text 
                            class="b-1 b-solid b-#FF679A rd-4.5 py-1 px-1.5 color-#FF679A"
                            @click="onPay(orderDetail.id)"
                        >立即付款</text>
                    </view>
                </template>
                <template v-else-if="orderDetail.state == 'MAKE_PAYMENT'">
                    <view class="text-3 flex gap-5">
                        <text class="border_xy py-1 px-1.5"
                            @click="applyRefundOrder(orderDetail.id)"
                        >申请退款</text>
                        <text 
                            class="b-1 b-solid b-#FF679A rd-4.5 py-1 px-1.5 color-#FF679A"
                        >待发货</text>
                    </view>
                </template>
                <template v-else-if="orderDetail.state == 'SHIP_ORDER'">
                    <view class="text-3 flex gap-5">
                        <text class="border_xy py-1 px-1.5"
                            @click="applyRefundOrder(orderDetail.id)"
                        >申请退款</text>
                        <text 
                            class="b-1 b-solid b-#FF679A rd-4.5 py-1 px-1.5 color-#FF679A"
                            @click="onReceive(orderDetail.id)"
                        >确认收货</text>
                    </view>
                </template>
                <template v-else-if="orderDetail.state == 'CONFIRM_RECEIPT'">
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
                            @click="delOrder(orderDetail.id)"
                        >删除订单</text>
                        <text class="b-1 b-solid b-#FF679A rd-4.5 py-1 px-1.5 color-#FF679A">{{ mapOrderState(orderDetail.state) }}</text>
                    </view>
                </template>
            </view>
        </view>

        <view class="bg-#fff my-3 rd-2">
            <view class="flex items-center pl-4 pt-3.75">
                <text class="w-6 h-6 bg-#FF679A rd-50% mr-4"></text>
                <view class="flex flex-col text-3.5 gap-1 color-#333">
                    <text v-if="intransitState.includes(orderDetail.state)">您的快递离开{{ orderDetail.productDetail.deliveryAddress }}, 已在途中</text>
                    <text v-else>{{ mapOrderState(orderDetail.state) }}</text>
                </view>
            </view>
            <u-divider hairline lineColor="#ccc" text=""></u-divider>
            <view class="flex items-center pl-4">
                <image
                    src="@/pagesA/static/map.svg"
                    mode="widthFix"
                    class="w-7 h-7 mr-4"
                />
                <template v-if="orderDetail.deliveryAddress && orderDetail.deliveryAddress.includes('name')">
                    <view class="flex flex-col text-3.5 gap-1 color-#333">
                        <text>送至 | {{ addressInfo.name }} {{ addressInfo.phone }}</text>
                        <text>{{ addressInfo.address }}</text>
                    </view>
                </template>
                <template v-else>
                    <text>送至 | {{ addressStr }}</text>
                </template>
            </view>
        </view>

        <view class="bg-#fff rd-2">
            <view class="flex flex-col p-3 text-3.5 gap-1">
                <text>订单编号:{{ orderDetail.orderNumber }}</text>
                <text>下单时间: {{ orderDetail.createTime }}</text>
            </view>
        </view>
    </view>
</template>

<style scoped>
    .item_card {
        padding: 12px;
        border-radius: 15px;
        background: #fff;
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