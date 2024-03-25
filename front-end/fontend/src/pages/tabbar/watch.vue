<script setup>
    import { getAllDiscoveryApi, likeOrCancelApi } from '@/api/tabbar/watch'
    const current = ref(1)

    const discoveryList = ref([])

    const getAllDiscovery = async (current = 1) => {
        const res = await getAllDiscoveryApi(current)
		const records = res.data.records
		const len = records.length
        if(len) {
            discoveryList.value = [ ...records ]
        } else {
            reachBottom.value = true
        }
    }

    const JumpDetail = () => {
        uni.navigateTo({
            url: '/pagesA/pages/wDetail/index'
        })
    }

    const onLikeOrCancel = async (discoveryId) => {
        const res = await likeOrCancelApi(discoveryId)
        const { data, message } = res
        if(data) {
            uni.showToast({
                title: message,
                icon: 'success',
                mask: true
            })
            getAllDiscovery(current.value)
        } else {
            uni.showToast({
                title: message,
                icon: 'error',
                mask: true
            })
        }
    }

    const reachBottom = ref(false)
	onReachBottom(() => {		
		current.value++
		getAllDiscovery(current.value)
	})

    onLoad(() => {
        getAllDiscovery()
    })
</script>

<template>
    <view class="bg-#f4f4f4">
        <template v-for="item of discoveryList" :key="item.id">
            <view class="bg-#fff mb-3">
                <view class="u-demo-block">
                    <view class="u-demo-block__content">
                        <view class="album" @click.stop="JumpDetail">
                            <view class="album__avatar">
                                <image
                                :src="item.user.avatar"
                                mode="aspectFit"
                                style="width: 32px;height: 32px;"
                                ></image>
                            </view>
                            <view class="album__content">
                                <u--text
                                :text="item.user.nickname"
                                type="primary"
                                bold
                                size="17"
                                ></u--text>
                                <u--text
                                margin="0 0 8px 0"
                                :text="item.descText"
                                ></u--text>
                                <view @click.stop>
                                    <u-album :urls="item.img"></u-album>
                                </view>
                            </view>
                        </view>
                    </view>
                </view>
                <u-divider text="" :hairline="true"></u-divider>
                <view class="h-8 flex justify-end px-10 gap-6">
                    <u-icon 
                        name="thumb-up" 
                        size="24"
                        :label="item.likes"
                        space="1"
                        @click="onLikeOrCancel(item.id)"
                    ></u-icon>
                    <!-- TODO 修改评论的数量 -->
                    <u-icon 
                        name="chat" 
                        size="24"
                        :label="item.comments?.length || 0" 
                        space="1"
                    ></u-icon>
                </view>
            </view>
        </template>

        <view 
			v-if="reachBottom"
			class="h-10 px-10"
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
             padding: 5px;
             border-radius: 3px;
             margin-left: 5px;
         }
    
        &__content {
            flex: 1;
            margin-left: 5px;
         }
    }
</style>