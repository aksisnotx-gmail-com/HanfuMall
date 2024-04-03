<script setup>
    import { 
        getSwiperListApi, 
        getRecommendProductsApi, 
        getSpecialProductsApi
    } from '@/api/home'
    import { useGoodsStore } from '@/store/modules/goods'
    import { useTypeStore } from '@/store/modules/type'

    const swiperList = ref([])
    const getSwiperList = async () => {
        const res = await getSwiperListApi()
        if(res.code !== 200) return

        const { records } = res.data
        swiperList.value = records.map(item => item.bannerUrl)
    }

    const noticeText = ref('欢迎同胞们来到绾青丝汉服社!')

    const recommendProducts = ref([])
    const getRecommendProducts = async () => {
        const res = await getRecommendProductsApi()
        if(res.data.records.length) {
            recommendProducts.value = [ ...res.data.records ]
        }
    }

    const specialProducts = ref([])
    const getSpecialProducts = async () => {
        const res = await getSpecialProductsApi()
        if(res.data.records.length) {
            specialProducts.value = [ ...res.data.records ]
        }
    }

    const goodsStore = useGoodsStore()
    const toDetail = (id) => {
        goodsStore.productId = id

        uni.navigateTo({
            url: '/pagesA/pages/goodsItem/index'
        })
    }

    const JumpClassify = (typeId) => {
        uni.navigateTo({
            url: `/pagesA/pages/classify/index?typeId=${typeId}`
        })
    }

    const JumpAllProduct = () => {
        uni.navigateTo({
            url: `/pagesA/pages/allProduct/index`
        })
    }

    const typeStore = useTypeStore()
    onMounted(async () => {
        await fetchFn()
        uni.hideLoading()

    })

    const fetchFn = async () => {
        await getSwiperList()
        await typeStore.getProductTypeList()
        await getRecommendProducts()
        await getSpecialProducts()
    }

    onLoad(() => {
        uni.showLoading({
            title: '加载中'
        });
    })

    onPullDownRefresh(async () => {
		uni.showLoading({
            title: '加载中'
        });
        await fetchFn()
        
		uni.stopPullDownRefresh()
		uni.hideLoading()
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
                :displayMultipleItems="0"
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
        <view class="flex justify-center py-6 px-2 bg-#fff">
            <view class="flex flex-wrap gap-3">
                <template v-for="item of typeStore.productTypeList" :key="item.id">
                    <button class="nav" @click="JumpClassify(item.id)">
                        <image class="w-12 h-12" :src="`/static/nav/${item.icon}.png`" mode="aspectFit" />
                        <text class="font-600 text-4.5">{{ item.type }}</text>
                    </button>
                </template>
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
                <template v-if="!specialProducts.length">
                    <u-empty
                        text="暂无商品数据"
                        mode="data"
                    >
                    </u-empty>
                </template>
                <template v-else>
                    <view 
                        v-for="item of specialProducts" 
                        :key="item.id" 
                        class="goods_item bg-hotpink"
                        @click="toDetail(item.productId)"
                    >
                        <view class="p-3 max-w-40 flex flex-col">
                            <image  
                                v-if="item.attribute.carouselUrl"
                                :src="item.attribute.carouselUrl" 
                                mode="aspectFit" 
                                class="w-30 h-30"
                            />
                            <text class="color-#999">
                                {{ item.attribute.desc }}
                            </text>
                            <view class="flex justify-between">
                                <text class="color-#DC143C font-600">¥ {{ item.specialPrice }}</text>
                                <text class="old_price">¥ {{ item.price }}</text>
                            </view>
                        </view>
                    </view>
                </template>
            </u-scroll-list>
              
        </view>
        <view class="bg-#fff px-4">
        <view class="text-5 font-600">热门推荐</view>
            <template v-if="!recommendProducts.length">
                <u-empty
                    text="暂无商品数据"
                    mode="data"
                >
                </u-empty>
            </template>
            <template v-else>
                <template v-for="item of recommendProducts" :key="item.product.id">
                    <view class="mt-5">
                        <view class="hot_card layout-items-center gap-2" @click="toDetail(item.product.id)">
                            <image :src="item.sku.attribute.carouselUrl" mode="aspectFit" class="w-30 h-30" />
                            <view class="flex flex-col">
                                <text class="color-#999 content">
                                    {{ item.product.productName }}
                                </text>
                                <text class="color-#DC143C font-600">
                                    ¥ {{ item.sku.specialPrice ? item.sku.specialPrice : item.sku.price }}
                                </text>
                            </view>
                        </view>
                    </view>
                </template>
            </template>
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

