<script setup>
	import { getProductByTypeApi } from '@/api/home'
	import { useGoodsStore } from '@/store/modules/goods'
    const goodsStore = useGoodsStore()

	const tabbar = reactive([
        {
            name: '汉服',
            proList: []
        },
        {
            name: '汉元素',
            proList: []
        },
		{
            name: '配饰周边',
            proList: []
        }
    ])

    const viewInfo = reactive({
        scrollTop: 0,
        current: 0,
        menuHeight: 0,
        menuItemHeight: 0
    })
        
    const swichMenu = (index) => {
        if(index == viewInfo.current) return;
		getProductByType(typeObj[Number(index)])

        viewInfo.current = index;
        // 如果为0，意味着尚未初始化
        if(viewInfo.menuHeight == 0 || viewInfo.menuItemHeight == 0) {
            getElRect('menu-scroll-view', 'menuHeight');
            getElRect('u-tab-item', 'menuItemHeight');
        }
        // 将菜单菜单活动item垂直居中
        viewInfo.scrollTop = index * viewInfo.menuItemHeight + viewInfo.menuItemHeight / 2 - viewInfo.menuHeight / 2;
    }

    const dataVal = ref(0)

    const getElRect = (elClass) => {
        new Promise((resolve, reject) => {
            const query = uni.createSelectorQuery()
            query.select('.' + elClass).fields({size: true},res => {
                // 如果节点尚未生成，res值为null，循环调用执行
                if(!res) {
                    setTimeout(() => {
                        getElRect(elClass);
                    }, 10);
                    return ;
                }
                dataVal.value = res.height;
                resolve(res.height)
            }).exec();
        })
    }

	const typeObj = {
		0: "HAN_FU",
		1: "HAN_YUAN_SU",
		2: "ACCESSORIES"
	}

    onLoad((option) => {
        const { index } = option
        viewInfo.current = Number(index)

		getProductByType(typeObj[index])
    })


	const onJumpDetail = (id) => {
		goodsStore.productId = id
		uni.navigateTo({
		    url: '/pagesA/pages/goodsItem/index'
		})
	}

	async function getProductByType (type) {
		if(!type) return

		tabbar[viewInfo.current].proList.splice(0, Infinity)

		const res = await getProductByTypeApi(type)
		const { records } = res.data
		const len = records.length
		if(len) {
			tabbar[viewInfo.current].proList = [ ...records ]
		}
	}
</script>


<template>
    <view class="u-menu-wrap">
        <scroll-view scroll-y scroll-with-animation class="u-tab-view menu-scroll-view" :scroll-top="viewInfo.scrollTop">
            <view 
				v-for="(item,index) in tabbar" 
				:key="index" 
				class="u-tab-item" 
				:class="[ viewInfo.current == index ? 'u-tab-item-active' : '']"
            	:data-current="index" 
				@click.stop="swichMenu(index)"
			>
                <text class="u-line-1">{{item.name}}</text>
            </view>
        </scroll-view>
        <template v-for="(item,index) of tabbar" :key="index">
            <scroll-view scroll-y class="right-box" v-if="viewInfo.current == index">
                <view class="page-view">
                    <view class="class-item">
                        <view class="item-container">
							<template v-if="!item.proList.length">
								<u-empty
									mode="data"
								>
								</u-empty>
							</template>
                            <template v-else>
								<template v-for="iten of item.proList" :key="iten.id">
									<template v-if="!iten.specCombinationList.length">
										<u-empty
											mode="data"
										>
										</u-empty>
									</template>
									<template v-else>
										<template v-for="product of iten.specCombinationList" :key="product.id">
											<view class="thumb-box" @click="onJumpDetail(iten.id)">
												<image class="item-menu-image" :src="product.carouselUrl" mode="aspectFit"></image>
												<view class="ml-3 h-100% flex flex-col justify-between font-600">
													<text>{{ product.desc }}</text>
													<text class="color-#FF0000 text-4.5">¥ {{ product.price }}</text>
												</view>
											</view>
										</template>
									</template>
								</template>
							</template>
                        </view>
                    </view>
                </view>
            </scroll-view>
        </template>
    </view>
</template>

<style scoped>
	.u-wrap {
		height: calc(100vh);
		/* #ifdef H5 */
		height: calc(100vh - var(--window-top));
		/* #endif */
		display: flex;
		flex-direction: column;
	}

	.u-search-box {
		padding: 18rpx 30rpx;
	}

	.u-menu-wrap {
		flex: 1;
		display: flex;
		overflow: hidden;
	}

	/* .u-search-inner {
		background-color: rgb(234, 234, 234);
		border-radius: 100rpx;
		display: flex;
		align-items: center;
		padding: 10rpx 16rpx;
	} */

	/* .u-search-text {
		font-size: 26rpx;
		margin-left: 10rpx;
	} */

	.u-tab-view {
		width: 260rpx;
		height: 100%;
	}

	.u-tab-item {
		height: 110rpx;
		background: #f6f6f6;
		box-sizing: border-box;
		display: flex;
		align-items: center;
		justify-content: center;
		color: #444;
		font-weight: 400;
		line-height: 1;
	}
	
	.u-tab-item-active {
		position: relative;
		color: #7794CF;
		font-size: 36rpx;
		font-weight: 600;
		background: #fff;
	}
	
	.u-tab-item-active::before {
		content: "";
		position: absolute;
		border-left: 4px solid #7794CF;
		height: 110rpx;
		left: 0;
		top: 0;
	}

	.u-tab-view {
		height: 100%;
	}
	
	.right-box {
		background-color: rgb(250, 250, 250);
	}
	
	.page-view {
		padding: 16rpx;
	}
	
	.class-item {
		background-color: #fff;
		padding: 16rpx;
		border-radius: 8rpx;
	}
	
	.item-title {
		font-size: 26rpx;
		color: hotpink;
		font-weight: bold;
	}
	
	.item-container {
		display: flex;
		flex-wrap: wrap;
	}
	
	.thumb-box {
		width: 100%;
		display: flex;
		align-items: center;
		justify-content: center;
		margin-top: 20rpx;
		padding-bottom: 20rpx;
		border-bottom: 1px solid #D8DEEB;
	}

	.item-menu-image {
		width: 200rpx;
		height: 200rpx;
	}
</style>
import { onLoad } from '@dcloudio/uni-app';

