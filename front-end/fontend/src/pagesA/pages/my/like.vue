<script setup>
    import { useMsgStore } from '@/store/modules/msg'

    import { readMsgApi } from '@/api/tabbar/my'

    const msgStore = useMsgStore()

    const { unReadLikes } = storeToRefs(msgStore)

    onUnload(async () => {
        
        const ids = unReadLikes.value.map(item => item.id)

        const params = {
            ids,
            type: 'LIKE'
        }
        await readMsgApi(params)
    })
</script>

<template>
    <view class="min-h-100vh bg-#F8F8F8">
        <template v-for="item of unReadLikes" :key="item.id">
            <template v-for="likeinfo of item.unreadLikeInfos" :key="likeinfo.id">
                <view class="bg-#fff mb-3 py-3">
                    <view class="flex justify-between items-center p-4">
                        <view class="flex items-center gap-3">
                            <view>
                                <up-avatar 
                                    :src="likeinfo.avatar"
                                ></up-avatar>
                            </view>
                            <view class="flex flex-col gap-1">
                                <text class="text-4.5 font-600">{{ likeinfo.nickname}}</text>
                                <text class="text-3 color-#333">{{ item.createTime }}</text>
                            </view>
                        </view>
                        <view class="color-#999">
                            点赞了你
                        </view>
                    </view>
                    <view class="bg-#F8F8F8 flex items-center gap-3 py-3 mx-3">
                        <image 
                            class="w-20 h-20" 
                            :src="item.img.length && item.img[0]" 
                            mode="aspectFit" 
                        />
                        <text class="text-3.5 color-#333">{{ item.descText }}</text>
                    </view>
                </view>
            </template>
        </template>
    </view>
</template>