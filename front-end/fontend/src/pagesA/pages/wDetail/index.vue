<script setup>
    const swiperList = ref([
        'https://cdn.uviewui.com/uview/swiper/swiper3.png',
        'https://cdn.uviewui.com/uview/swiper/swiper2.png',
        'https://cdn.uviewui.com/uview/swiper/swiper1.png',
    ])

	const reachBottom = ref(false)
	onReachBottom(() => {
		reachBottom.value = true
	})
</script>

<template>
    <view class="bg-#ccc">
        <view class="album__avatar">
            <image
            src="https://ijry.github.io/uview-plus/h5/assets/logo-8d54bbeb.png"
            mode="aspectFit"
            style="width: 32px;height: 32px;"
            ></image>
            <text>昵称2</text>
        </view>

        <view class="px-4 py-3">
            <u-swiper
                :list="swiperList"
                indicator
                circular
                indicatorActiveColor="#E4697B"
                indicatorMode="dot"
            ></u-swiper>
        </view>

        <view class="bg-#fff p-3 flex flex-col gap-5">
            <text>自定义指示器模式, 可配置指示器样式3D轮播图效果, 满足不同的开发需求可配置显示标题, 涵盖不同的应用场景具有设置加载状态和嵌入视频的能力，功能齐全丰富</text>
            <text class="color-#999">2023/10/3</text>
        </view>

     
        <view class="bg-#fff mt-1 p-2">
            <block v-for="item of 2" :key="item">
                <view class="mb-3">
                    <view class="flex color-#ccc">
                        <image
                        src="https://ijry.github.io/uview-plus/h5/assets/logo-8d54bbeb.png"
                        mode="aspectFit"
                        style="width: 32px;height: 32px;"
                        class="mr-3"
                        ></image>
                        <view class="flex-1">
                            <view class="flex justify-between">
                                <text>昵称1</text>
                                <text>2023/10/5/12:03</text>
                            </view>
                            <view class="my-2">
                                评论内容
                            </view>
                        </view>
                    </view>
                    <view class="bg-#F2F2F2 ml-8 color-#999 py-4 pl-3">
                        昵称2回复昵称1: 回复文本内容
                    </view>
                </view>
            </block>
        </view>

        <view 
			class="bg-#fff"
		>
			<u-divider 
				text="已经到底啦~" 
				hairline
				textColor="#999"
				lineColor="#999"
			></u-divider>
		</view>
    </view>
</template>

<style scoped lang="scss">
    .album {
        @include flex;
        align-items: flex-start;

        &__avatar {
            background-color: #fff;
            padding: 5px;
            border-radius: 3px;
            display: flex;
            align-items: center;
            color: #999;
            gap: 20px;
            padding: 20px 10px;
         }
    
        &__content {
            flex: 1;
            margin-left: 5px;
        }
    }

    :deep(.u-divider) {
        margin: 0px !important;
    }
</style>