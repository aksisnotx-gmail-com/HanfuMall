<script setup>
    import { getAllCommentApi, commentApi } from '@/api/tabbar/watch'
    import { useCommentStore } from '@/store/modules/comment'
    import CommentItem from '@/components/comment-item';

    const discoveryInfo = reactive({
        avatar: '',
        nickname: '',
        img: [],
        createTime: '',
        descText: ''
    })
    const commentList = ref([])
    const getCommentList = async (id) => {
        const res = await getAllCommentApi(id)
        const {
            user,
            img,
            createTime,
            descText,
            comments
        } = res.data
        discoveryInfo.avatar = user?.avatar || ''
        discoveryInfo.nickname = user?.nickname || ''
        discoveryInfo.img = img
        discoveryInfo.createTime = createTime
        discoveryInfo.descText = descText
        commentList.value = [ ...comments ]
    }

    const commentInfo = reactive({
        commentShow: false,
        commentVal: '',
        commentType: '',
        commentId: ''
    })
    const open = () => {
        commentInfo.commentShow = true
    }
    const close = () => {
        commentInfo.commentShow = false
        commentInfo.commentVal = ''
    }

    const onComment = (commentType) => {
        commentInfo.commentShow = true
        commentInfo.commentType = commentType
        commentInfo.commentId = commentStore.discoveryId
        
    }

    const confirm = async () => {
        const content = commentInfo.commentVal
        if (!content) return uni.$u.toast('请输入评论内容')

        const commentId = commentInfo.commentId
        const commentType = commentInfo.commentType


        const objParam = {
            commentId,
            content,
            commentType
        }
        const res = await commentApi(objParam)
        const { data, message } = res
        if(data) {
            uni.showToast({
                title: '评论成功',
                icon: 'success',
                mask: true,
                duration: 3000
            })
            getCommentList(commentId)
            commentInfo.commentShow = false
            commentInfo.commentVal = ''
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

    const commentStore = useCommentStore()
    onLoad(() => {
        getCommentList(commentStore.discoveryId)
    })
</script>

<template>
    <view class="bg-#ccc">
        <view class="album__avatar">
            <image
            :src="discoveryInfo?.avatar"
            mode="aspectFit"
            style="width: 32px;height: 32px;"
            ></image>
            <text>{{ discoveryInfo?.nickname }}</text>
        </view>

        <view class="px-4 py-3">
            <u-swiper
                :list="discoveryInfo?.img"
                indicator
                circular
                indicatorActiveColor="#E4697B"
                indicatorMode="dot"
                :displayMultipleItems="0"
            ></u-swiper>
        </view>

        <view class="bg-#fff p-3 flex flex-col">
            <text>{{ discoveryInfo.descText }}</text>
        </view>
        <view class="bg-#fff flex justify-between items-center px-3">
            <text class="color-#999">{{ discoveryInfo.createTime }}</text>
            <u-icon 
                name="chat" 
                size="24"
                :label="commentList.length" 
                space="1"
                @click="onComment('COMMENT')"
            ></u-icon>
        </view>
        
        <view class="bg-#fff mt-1 p-3">
            <CommentItem 
                :commentList="commentList"
            />
        </view>

        
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

    .border_l {
        border-left: 1px solid #ccc;
    }
</style>