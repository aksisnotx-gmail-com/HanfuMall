<script setup>
    import { commentApi } from '@/api/tabbar/watch'
    import { useCommentStore } from '@/store/modules/comment'

    const emits = defineEmits(['updateCommentList'])

    const commentStore = useCommentStore()

    const props = defineProps({
        commentList: {
            type: Array,
            default: () => []
        },
        parentNickname: {
            type: String,
            default: ''
        }, // 表示父级评论者的昵称
        isTopLevel: { // 新增标志，用于判断是否为顶层评论
            type: Boolean,
            default: true, // 默认为顶层，因为最初的调用通常针对顶层评论
        }
    });

    const commentInfo = reactive({
        commentShow: false,
        commentVal: '',
        commentType: 'REPLY',
        commentId: ''
    })

    const open = () => {
        commentInfo.commentShow = true
    }
    const close = () => {
        commentInfo.commentShow = false
        commentInfo.commentVal = ''
    }
    const onReply = (reply) => {
        const { id } = reply
        commentInfo.commentId = id

        commentInfo.commentShow = true
    }

    const confirm = async () => {
        const content = commentInfo.commentVal
        if (!content) return uni.$u.toast('请输入回复内容')

        const commentId = commentInfo.commentId
        const commentType = commentInfo.commentType
        const discoveryId = commentStore.discoveryId

        const objParam = {
            discoveryId,
            commentId,
            content,
            commentType
        }

        const res = await commentApi(objParam)
        const { data, message } = res
        if(data) {
            uni.showToast({
                title: '回复成功',
                icon: 'success',
                mask: true,
                duration: 3000
            })

            commentInfo.commentShow = false
            commentInfo.commentVal = ''
            emits('updateCommentList')
        } else {
            uni.showToast({
                title: message,
                icon: 'error',
                mask: true,
                duration: 3000
            })
            commentInfo.commentShow = false
            commentInfo.commentVal = ''
        }
    }
</script>

<template>
    <view>
        <template v-for="reply of commentList" :key="reply.id">
            <view class="mb-3">
                <!-- 仅在顶层评论时显示头像和昵称等信息 -->
                <template v-if="isTopLevel">
                    <view class="flex color-#ccc mb-2">
                        <image :src="reply.user.avatar" mode="aspectFit" style="width: 32px;height: 32px;" class="mr-3"></image>
                        <view class="flex-1">
                            <view class="flex justify-between mb-2">
                                <text>{{ reply.user.nickname }}</text>
                                <text>{{ reply.createTime }}</text>
                            </view>
                            <text @click="onReply(reply)">{{ reply.content }}</text>
                        </view>
                    </view>
                </template>
                <view 
                    class="bg-#F2F2F2 ml-8 mb-2 px-2 py-4" 
                    v-if="!isTopLevel"
                    @click="onReply(reply)"
                >
                    <!-- 在所有回复中显示内容 -->
                    <template v-if="parentNickname">
                        <text>{{ reply.user.nickname }} 回复 {{ parentNickname }}: {{ reply.content }}</text>
                    </template>
                    <template v-else>
                        <text>{{ reply.content }}</text>
                    </template>
                </view>
                <template v-if="reply.replies.length">
                    <view class="color-#999">
                        <!-- 递归调用时将isTopLevel设置为false -->
                        <CommentItem 
                            :commentList="reply.replies" 
                            :parentNickname="reply.user.nickname" 
                            :isTopLevel="false"
                            @updateCommentList="emits('updateCommentList')"
                        />
                    </view>
                </template>
            </view>
        </template>

        <u-popup 
            :show="commentInfo.commentShow"
            round="10"
            @close="close" 
            @open="open"
            mode="center"
            :safeAreaInsetBottom="false"
        >
            <view class="w-60">
                <u--textarea 
                    v-model="commentInfo.commentVal" 
                    placeholder="请输入友好评论"
                >
                </u--textarea>

                <view class="flex text-4.5 font-600">
                    <text 
                        class="h-10 flex-1 flex justify-center items-center color-#000"
                        @click="close"
                    >
                    取消</text>
                    <text 
                        class="h-10 flex-1 flex justify-center items-center color-#596B94 border_l"
                        @click="confirm"
                    >发送</text>
                </view>
            </view>
		</u-popup>
    </view>
</template>