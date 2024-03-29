<script setup>
    import { useProCommentStore } from '@/store/modules/proComment'
    const proCommentStore = useProCommentStore()

    const { commentList, pager } = storeToRefs(proCommentStore)

    const id = ref('')
    onLoad((options) => {
        const { productId } = options
        if(!productId) return

        id.value = productId
        proCommentStore.getCommentListById(productId)
    })

    onReachBottom(async () => {		
		uni.showLoading({
            title: '加载中'
        });

		const currentTotal = pager.value.current * pager.value.size
		if(currentTotal < pager.value.total) {
            pager.value.current++
            await proCommentStore.getCommentListById(id, pager.value.current)
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
</script>

<template>
    <view class="bg-#F2F2F2 p-3">
        <template v-if="!commentList.length">
            <view class="h-100vh flex justify-center items-center">
                <u-empty
                    mode="data"
                    text="暂无评论"
                >
                </u-empty>
            </view>
        </template>
        <template v-else>
            <template v-for="item of commentList" :key="item.id">
                <view class="item_card py-3 mb-3">
                    <view class="flex max-h-80 overflow-hidden p-3">
                        <up-avatar 
                            :src="item.user.avatar" 
                            shape="circle"
                        ></up-avatar>
                        <view class="flex flex-col ml-2">
                            <view class="w-70 flex justify-between">
                                <text class="text-3 color-#999">{{ item.user.nickname }}</text>
                                <text class="text-3 color-#999">{{ item.createTime }}</text>
                            </view>
                            <view class="my-1">
                                <u-rate
                                    :modelValue="item.starRating"
                                ></u-rate>
                            </view>
                                
                            <view class="ellipsis my-2">
                                {{ item.commentContent }}
                            </view>
                            <view class="flex gap-3">
                                <template v-if="item.commentImgUrl && item.commentImgUrl.length">
                                    <template v-for="url of item.commentImgUrl" :key="url">
                                        <up-image 
                                            show-loading 
                                            :src="url" 
                                            width="80px" 
                                            height="80px"
                                        ></up-image>
                                    </template>
                                </template>

                            </view>
                        </view>
                    </view>
                </view>
            </template>
        </template>
    </view>
</template>

<style scoped>
    .item_card {
        height: 254px;
        border-radius: 30px;
        background: #fff;
        box-shadow: 20px 20px 60px #bebebe,
                    -20px -20px 60px #ffffff;
    }

    .ellipsis {
        display: -webkit-box;
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 5; /* 限制在3行内 */
        overflow: hidden;
        /* max-height: 100px; 示例最大高度，根据需要调整 */
        text-overflow: ellipsis;
        white-space: normal;
        word-break: break-word;
    }
</style>