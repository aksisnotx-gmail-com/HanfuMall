<script setup>
    import { useMsgStore } from '@/store/modules/msg'

    const msgStore = useMsgStore()

    const { unReadLikes, unReadComments } = storeToRefs(msgStore)

    const list = reactive([
        {
            id: 1,
            icon: 'good.svg',
            title: '点赞',
            count: 0
        },
        {
            id: 2,
            icon: 'comment.svg',
            title: '评论',
            count: 0
        }
    ])

    const onClickFunction = (item) => {
        const { title, count } = item
        if(!count) return

        const obj = {
            '点赞': '/pagesA/pages/my/like',
            '评论': '/pagesA/pages/my/cmt'
        }

        uni.navigateTo({
            url: obj[title]
        })
    }

    onMounted(() => {
        
    })

    onLoad(() => {
       list[0].count = unReadLikes.value.length || 0
       list[1].count = unReadComments.value.length || 0
    })
</script>

<template>
    <view class="bg-#fff px-3 pt-3">
       <template v-for="item of list" :key="item.id">
            <view class="flex items-center mb-4" @click="onClickFunction(item)">
                <image 
                    :src="'/pagesA/static/' + item.icon" 
                    mode="widthFix"
                    class="w-10 h-10"
                >
                </image>
                <view 
                    :class="[
                        'h-11', 'ml-3', 'flex-1', 
                        'flex', 'justify-between', 
                        'items-center', 
                        'border_b'
                        ]"
                    >
                    <text class="color-#666">{{ item.title }}</text>
                    <view class="flex justify-center items-center">
                        <view 
                            class="badge" 
                            v-if="item.count != 0"
                        >
                            {{ item.count }}</view>
                        <u-icon 
                        name="arrow-right" 
                        color="#FC7DA6" 
                        size="18"
                        ></u-icon>
                    </view>
                </view>
            </view>
       </template>
    </view>
</template>

<style scoped>
    .border_b {
        border-bottom: 1px solid #ccc;
    }

    .badge {
        display: flex;
        justify-content: center;
        align-items: center;
        width: 16px;
        height: 16px;
        background: #FC7DA6;
        border-radius: 50%;
        font-size: 12px;
        color: #fff;
    }
</style>