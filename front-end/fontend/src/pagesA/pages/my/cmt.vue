<script setup>
    import { useMsgStore } from '@/store/modules/msg'
    // import { getByTypeApi } from '@/api/tabbar/my'
    import { readMsgApi } from '@/api/tabbar/my'

    const msgStore = useMsgStore()
    const { unReadComments } = storeToRefs(msgStore)


    onUnload(async () => {
        const ids = unReadComments.value.map(item => item.id)
        const params = {
            ids,
            type: 'COMMENT_OR_REPLY'
        }
        await readMsgApi(params)
    })

    /**
     * @param {String} dateStr 日期字符串 格式为 "yyyy-MM-dd HH:mm:ss"
     * @description 获取相对时间，如"1分钟前"
     * @returns {String} 返回相对时间，如"1分钟前"
     */
    function getTimeAgo(dateStr) {
    const now = new Date();
    const inputDate = new Date(dateStr);

    let diffInMs = now - inputDate;

    // 计算相差的分钟数
    let diffInMinutes = Math.floor(diffInMs / (1000 * 60));

    if (diffInMinutes < 60) {
        return `${diffInMinutes}分钟前`;
    } else if (diffInMinutes < 120) {
        return '1小时前';
    } 

    // 计算相差的小时数
    let diffInHours = Math.floor(diffInMinutes / 60);
    if (diffInHours < 24) {
        return `${diffInHours}小时前`;
    } else if (diffInHours < 48) {
        return '1天前';
    }

    // 计算相差的天数
    let diffInDays = Math.floor(diffInHours / 24);
    if (diffInDays <= 30) { // 这里假设一个月大约为30天
        return `${diffInDays}天前`;
    } else if (diffInDays <= 60) { // 这里假设两个月大约为60天
        return '1个月前';
    }

    // 计算相差的月数（这里简化处理，未考虑月份天数差异）
    let diffInMonths = Math.floor(diffInDays / 30);
    if (diffInMonths <= 12) {
        return `${diffInMonths}个月前`;
    } else {
        // 计算相差的年数
        let diffInYears = Math.floor(diffInMonths / 12);
        return `${diffInYears}年前`;
    }
    }
</script>

<template>
    <view class="min-h-100vh bg-#F8F8F8">
        <template v-for="item of unReadComments" :key="item.id">
            <view class="bg-#fff">
                <view class="flex justify-between items-center p-4">
                    <view class="flex items-center gap-3">
                        <view>
                            <template v-if="item.user">
                                <up-avatar 
                                    :src="item.avatar"
                                ></up-avatar>
                            </template>
                        </view>
                        <view class="flex flex-col gap-1">
                            <text class="text-4.5 font-600">{{ item.user.nickname }}</text>
                            <text class="text-3 color-#333">{{ getTimeAgo(item.createTime) }}</text>
                        </view>
                    </view>
                </view>
                <view class="bg-#F8F8F8 py-3 mx-3" v-if="item.user">
                    {{ item.user.nickname }} 回复的内容为: {{ item.content }}
                </view>
                <view class="bg-#F8F8F8 flex flex-col gap-3 py-3 mx-3">
                    <text></text>
                    <view class="flex items-center gap-3">
                        <template v-if="item.discovery.img && item.discovery.img.length">
                            <image 
                                class="w-20 h-20"
                                :src="item.discovery.img[0]" 
                                mode="aspectFit" 
                            />
                        </template>
                        <text class="text-3.5 color-#333">{{ item.discovery.descText }}</text>
                    </view>
                </view>
            </view>
        </template>
    </view>
</template>