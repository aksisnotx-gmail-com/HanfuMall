<script setup>
    import { getSwiperListApi } from '@/api/home'
    import { useGoodsStore } from '@/store/modules/goods.js'

    const swiperList = ref([])
    const getSwiperList = async () => {
        const res = await getSwiperListApi()
        const { records } = res.data
        swiperList.value = records.map(item => item.bannerUrl)
    }

    const noticeText = ref('欢迎同胞们来到绾青丝汉服社!')

    const list = [
        {
            thumb: "https://cdn.uviewui.com/uview/goods/1.jpg"
        }, 
        {
            thumb: "https://cdn.uviewui.com/uview/goods/2.jpg"
        }, 
        {
            thumb: "https://cdn.uviewui.com/uview/goods/3.jpg"
        }, 
        {
            thumb: "https://cdn.uviewui.com/uview/goods/4.jpg"
        }, 
        {
            thumb: "https://cdn.uviewui.com/uview/goods/5.jpg"
        }
    ]

    const goodsStore = useGoodsStore()
    const toDetail = (item) => {
        console.log(item, 'item');
        goodsStore.goodsItem = { ...item }
        // TODO item
        uni.navigateTo({
            url: '/pagesA/pages/goodsItem/index'
        })
    }

    const JumpClassify = (index) => {
        uni.navigateTo({
            url: `/pagesA/pages/classify/index?index=${index}`
        })
    }

    const JumpAllProduct = () => {
        uni.navigateTo({
            url: `/pagesA/pages/allProduct/index`
        })
    }

    onMounted(() => {
        getSwiperList()  
    })
</script>

<template>
    <view class="bg-#C1C1C1">
        <view class="px-4 py-3">
            <u-swiper
                :list="swiperList"
                indicator
                circular
                indicatorActiveColor="#E4697B"
                indicatorMode="dot"
            ></u-swiper> 
        </view>
        <view>
            <u-notice-bar 
                :text="noticeText"
                bgColor="#DFE7FA"
                fontSize="16"
                icon="volume-fill"
            ></u-notice-bar>
        </view>
        <view class="flex justify-center gap-6 py-6 bg-#fff">
            <view class="flex flex-col gap-6">
                <button class="nav" @click="JumpClassify(0)">
                    <image class="w-12 h-12" src="@/static/nav/hf.png" mode="aspectFit" />
                    <text class="font-600 text-4.5">汉服专区</text>
                </button>
                <button class="nav" @click="JumpClassify(2)">
                    <image class="w-12 h-12" src="@/static/nav/fan.png" mode="aspectFit" />
                    <text class="font-600 text-4.5">配饰周边</text>
                </button>
            </view>
            <view class="flex flex-col gap-6">
                <button class="nav px-10" @click="JumpClassify(1)">
                    <image class="w-12 h-12" src="@/static/nav/hy.png" mode="aspectFit" />
                    <text class="font-600 text-4.5">汉元素</text>
                </button>
                <button class="nav" @click="JumpAllProduct">
                    <image class="w-12 h-12" src="@/static/nav/all.png" mode="aspectFit" />
                    <text class="font-600 text-4.5">全部商品</text>
                </button>
            </view>
        </view>
        <view class="bg-#fff px-4">
            <view class="layout-slide mb-2">
                <text class="text-5 font-600">限时特惠</text>
                <text class="color-#ccc">滑动查看</text>
            </view>
              
            <u-scroll-list
                :indicator="false"
            >
                <view 
                    v-for="(item, index) in list" 
                    :key="index" 
                    class="goods_item bg-hotpink"
                    @click="toDetail(item)"
                >
                    <view class="p-3">
                        <image :src="item.thumb" mode="aspectFit" class="w-30 h-30" />
                        <text class="color-#999">
                            唐圆领袍复原款等运来
                        </text>
                        <view class="flex justify-between">
                            <text class="color-#DC143C font-600">¥ 89.10</text>
                            <text class="old_price">¥ 119</text>
                        </view>
                    </view>
                </view>
            </u-scroll-list>
              
        </view>
        <view class="bg-#fff px-4">
            <view class="text-5 font-600">热门推荐</view>
            <view class="pb-2">
                <view class="hot_card layout-slide">
                    <image src="https://cdn.uviewui.com/uview/goods/1.jpg" mode="aspectFit" class="h-30" />
                    <view class="flex flex-col">
                        <text class="color-#999 content">
                            原创宋制汉服女重工刺绣长衫精子齐腰褶裙春夏款原创宋制汉服女重工刺绣长衫精子齐腰褶裙春夏款
                        </text>
                        <text class="color-#DC143C font-600">
                            ¥ 29.00 - 228.00
                        </text>
                    </view>
                </view>
                <view class="hot_card layout-slide">
                    <image src="https://cdn.uviewui.com/uview/goods/1.jpg" mode="aspectFit" class="h-30" />
                    <view class="flex flex-col">
                        <text class="color-#999 content">
                            原创宋制汉服女重工刺绣长衫精子齐腰褶裙春夏款原创宋制汉服女重工刺绣长衫精子齐腰褶裙春夏款
                        </text>
                        <text class="color-#DC143C font-600">
                            ¥ 29.00 - 228.00
                        </text>
                    </view>
                </view>
            </view>
        </view>
    </view>
</template>

<style scoped>
    :deep(.u-notice__content__text text) {
        color: #5D99C0 !important;
    }

    .nav {
        min-width: 170px;
        display: flex;
        padding: 10px 18px;
        color: #090909;
        font-size: 18px;
        border-radius: 20px;
        /* background: #e8e8e8; */
        background-color: #fff;
        cursor: pointer;
        border: 1px solid #e8e8e8;
        transition: all 0.3s;
        box-shadow: 6px 6px 12px #c5c5c5, -6px -6px 12px #ffffff;
    }

    /* .nav:hover {
        border: 1px solid white;
    }

    .nav:active {
        box-shadow: 4px 4px 12px #c5c5c5, -4px -4px 12px #ffffff;
    } */

    .goods_item {
        margin-left: 20px;
        background: white;
        border-radius: 10px;
        transition: border-radius 0.5s cubic-bezier(0.175, 0.885, 0.32, 1.275);
        box-shadow: inset 0 -3em 3em rgba(0,0,0,0.1),
             0 0  0 2px rgb(190, 190, 190),
             0.3em 0.3em 1em rgba(0,0,0,0.3);
    }

    .goods_item:first-child {
        margin-left: 0;
    }

    .old_price {
        text-decoration: line-through;
        font-size: 14px;
        font-weight: 500;
    }

    .hot_card {
        padding: 10px;
        border-radius: 20px;
        background: #e0e0e0;
        box-shadow: 20px 20px 60px #bebebe,
                -20px -20px 60px #ffffff;
        margin: 10px 0;
    }

    .content {
        font-size: 14px;
        /* 设置最大高度为300px */
        max-height: 80px;
        /* 隐藏超出的内容 */
        overflow: hidden;
        /* 显示省略号来表示被截断的文本，需要设置文本的装饰方式 */
        display: -webkit-box;
        -webkit-line-clamp: 4; /* 用来限制在一个块元素显示的文本的行数 */
        -webkit-box-orient: vertical;
        text-overflow: ellipsis;
        /* 需要设置这个才能使得省略号生效 */
        white-space: normal;
    }
</style>

