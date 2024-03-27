<script setup>
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
                            <text>{{ reply.content }}</text>
                        </view>
                    </view>
                </template>
                <view class="bg-#F2F2F2 ml-8 mb-2 px-2 py-4" v-if="!isTopLevel">
                    <!-- 在所有回复中显示内容 -->
                    <template v-if="parentNickname">
                        <text>{{ parentNickname }} 回复 {{ reply.user.nickname }}: {{ reply.content }}</text>
                    </template>
                    <template v-else>
                        <text>{{ reply.content }}</text>
                    </template>
                </view>
                <template v-if="reply.replies.length">
                    <view class="color-#999">
                        <!-- 递归调用时将isTopLevel设置为false -->
                        <CommentItem :commentList="reply.replies" :parentNickname="reply.user.nickname" :isTopLevel="false" />
                    </view>
                </template>
            </view>
        </template>
    </view>
</template>