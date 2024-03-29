<script setup>
    import { getAllDiscoveryApi, likeOrCancelApi } from '@/api/tabbar/watch'
    import { useCommentStore } from '@/store/modules/comment'

    const pageInfo = reactive({
        current: 1,
        size: 20,
        total: 0
    })

    const discoveryList = ref([])

    const getAllDiscovery = async (currentt = 1) => {
        const res = await getAllDiscoveryApi('ALL', currentt)
		const { records, current, size, total } = res.data
		const len = records.length
        if(len) {
            pageInfo.current = current
            pageInfo.size = size
            pageInfo.total = total
            discoveryList.value = [ ...discoveryList.value, ...records ]
        }
    }

    const commentStore = useCommentStore()
    const JumpDetail = (id) => {
        commentStore.discoveryId = id
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
            getAllDiscovery(pageInfo.current)
        } else {
            uni.showToast({
                title: message,
                icon: 'error',
                mask: true
            })
        }
    }

	onReachBottom(async () => {		
        uni.showLoading({
            title: '加载中'
        });
		const currentTotal = pageInfo.current * pageInfo.size
		if(currentTotal < pageInfo.total) {
            pageInfo.current++
            await getAllDiscovery(pageInfo.current)
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

    onMounted(async () => {
        await getAllDiscovery()
        uni.hideLoading()
    })

    onLoad(() => {
        uni.showLoading({
            title: '加载中'
        })
    })

    onPullDownRefresh(async () => {
        const len = discoveryList.value.length
		discoveryList.value.splice(0, len)

		uni.showLoading({
            title: '加载中'
        });
        await getAllDiscovery()
        
		uni.stopPullDownRefresh()
		uni.hideLoading()
	})
</script>

<template>
    <view class="bg-#f4f4f4">
        <template v-for="item of discoveryList" :key="item.id">
            <view class="bg-#fff mb-3">
                <view class="u-demo-block">
                    <view class="u-demo-block__content">
                        <view class="album" @click.stop="JumpDetail(item.id)">
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
                    <u-icon 
                        name="chat" 
                        size="24"
                        :label="item.comments?.length || 0" 
                        space="1"
                        @click="JumpDetail(item.id)"
                    ></u-icon>
                </view>
            </view>
        </template>
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