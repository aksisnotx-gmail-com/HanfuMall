<script setup>
    import { getMyEvaluateApi, delMyEvaluateApi } from '@/api/tabbar/my'

    const pageInfo = reactive({
        current: 1,
        size: 20,
        total: 0
    })

    const evaluateList = ref([])
    const getMyEvaluate = async (currentt = 1) => {
        const res = await getMyEvaluateApi(currentt)
		const { records, total, size, current } = res.data
		pageInfo.current = current
		pageInfo.size = size
		pageInfo.total = total
        evaluateList.value = [ ...evaluateList.value, ...records ]
    }

    const delEvaluate = (commentId, orderId) => {
        uni.showModal({
            title: '提示',
            content: '确定要删除该评论吗？',
            success: async ({ confirm }) => {
                if(confirm) {
                    const res = await delMyEvaluateApi(commentId, orderId)
                    if(res.code === 200) {
                        
                        await getMyEvaluate(pageInfo.current)

                        uni.showToast({
                            title: '删除成功',
                            icon: 'success',
                            duration: 2000
                        })
                    }
                }
            }
        })
    }

    onReachBottom(async () => {
        if(!evaluateList.value.length) return
        
		uni.showLoading({
            title: '加载中'
        });
		const currentTotal = pageInfo.current * pageInfo.size
		if(currentTotal < pageInfo.total) {
            pageInfo.current++
            await getMyEvaluate(pageInfo.current)
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
        await getMyEvaluate()
        uni.hideLoading()
    })

    onLoad(() => {
        uni.showLoading({
            title: '加载中'
        })
    }) 
</script>

<template>
    <view class="bg-#f2f2f2 pb-6">
        <template v-if="!evaluateList.length">
            <view class="h-100vh flex justify-center items-center">
                <u-empty
                mode="data"
                >
            </u-empty>
            </view>
        </template>
        <template v-for="item of evaluateList" :key="item.comment.id">
            <view class="bg-#fff px-3 mb-3">
                <view 
                    class="h-10 layout-slide font-600 color-#666"
                >
                    <text class="text-3.5">{{ item.comment.createTime }}</text>
                    <text @click="delEvaluate(item.comment.id, item.order.id)">删除</text>
                </view>
                <view class="color-#666 flex flex-col">
                    <text class="comment">{{ item.comment.commentContent }}</text>
                    <view class="flex justify-around">
                        <template v-if="item.comment.commentImgUrl">
                            <template v-for="url of item.comment.commentImgUrl" :key="url">
                                <image
                                    class="w-25 h-20"
                                    :src="url"
                                    mode="aspectFit"
                                />
                            </template>
                        </template>
                    </view>
                </view>
                <u-divider text="" hairline></u-divider>
                <view class="flex gap-3 pb-5">
                    <image
                        class="w-25 h-15"
                        v-if="item.order.productSku"
                        :src="item.order.productSku.attribute.carouselUrl" 
                        mode="aspectFit"
                    />
                    <view class="flex flex-col gap-1">
                        <text class="text-3 font-600">{{ item.order.productDetail.productName }}</text>
                        <text class="text-3 color-#999">{{ item.order.productSku.attribute.desc }}</text>
                    </view>
                </view>
            </view>
        </template>
    </view>
</template>

<style scoped>
    .comment {
    word-wrap: break-word; /* 允许在长单词或URL地址内部进行断行 */
    padding: 10px; /* 可选：添加一些内边距，使文字不会紧贴容器边缘 */
    /* background-color: #f9f9f9; 可选：设置背景颜色，使评论更加突出 */
    line-height: 1.5; /* 可选：设置行高，提高阅读舒适度 */
    }
</style>