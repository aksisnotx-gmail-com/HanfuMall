<template>
    <view class="h-100vh bg-#f4f4f4 px-2 pt-3">
        <view 
            class="item_card"
            @click="JumpDetail"
        >
            <view class="flex gap-4">
                <image
                    src="data:image/jpeg;base64,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"
                    mode="aspectFill"
                    class="img"
                />
                <view class="w-60 flex flex-col justify-between">
                    <text class="name">原创宋制汉服女重工刺绣长衫精子齐腰褶裙春夏款</text>
                    <view class="flex items-center gap-3 text-3 color-#666">
                        <text>暗红褶子红色</text>
                        <text>s</text>
                        <text class="font-600">x 1</text>
                    </view>
                    <view class="layout-slide">
                        <text class="color-#DC143C font-600">¥ 69.00</text>
                        <text class="text-3.5 color-#FF679A">待付款</text>
                    </view>
                </view>
            </view>
            <view class="mt-3 pt-2 flex flex-col items-end gap-3 border_t">
                <view class="flex gap-3">
                    <text class="text-3.5">共1件商品, 买家实付</text>
                    <text class="color-#DC143C font-600">¥ 69.00</text>
                </view>
                <view class="text-3 flex gap-5">
                    <text class="border_xy py-1 px-1.5">关闭订单</text>
                    <text class="b-1 b-solid b-#FF679A rd-4.5 py-1 px-1.5 color-#FF679A">立即付款</text>
                </view>
            </view>
        </view>

        <view class="bg-#fff my-3 rd-2">
            <view class="flex items-center pl-4 pt-3.75">
                <text class="w-6 h-6 bg-#FF679A rd-50% mr-4"></text>
                <view class="flex flex-col text-3.5 gap-1 color-#333">
                    <text>运输中 01/01 23:00</text>
                    <text>您的快递离开**, 已发往**</text>
                </view>
            </view>
            <u-divider hairline lineColor="#ccc" text=""></u-divider>
            <view class="flex items-center pl-4">
                <image
                    src="@/pagesA/static/map.svg"
                    mode="widthFix"
                    class="w-7 h-7 mr-4"
                />
                <view class="flex flex-col text-3.5 gap-1 color-#333">
                    <text>送至 | 姓名 电话</text>
                    <text>目标地址</text>
                </view>
            </view>
        </view>

        <view class="bg-#fff rd-2">
            <view class="flex flex-col p-3 text-3.5 gap-1">
                <text>订单编号: 123456789809</text>
                <text>下单时间: 2020/12/30 11:33:11</text>
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