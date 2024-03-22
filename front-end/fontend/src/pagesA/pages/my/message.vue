<script setup>
    const list = [
        {
            id: 1,
            icon: 'good.svg',
            title: '点赞'
        },
        {
            id: 2,
            icon: 'comment.svg',
            title: '评论'
        }
    ]

    const onClickFunction = (item) => {
        console.log(item, 'item')
    }
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
                    <u-icon 
                    name="arrow-right" 
                    color="#FC7DA6" 
                    size="18"
                    ></u-icon>
                </view>
            </view>
       </template>
    </view>
</template>

<style scoped>
    .border_b {
        border-bottom: 1px solid #ccc;
    }
</style>