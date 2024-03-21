<script setup>

</script>

<template>
    <view class="bg-#f2f2f2 pb-6">
        <template v-for="item of 3" :key="item">
            <view class="bg-#fff px-3 mb-3">
                <view 
                    class="h-10 layout-slide font-600 color-#666"
                >
                    <text class="text-3.5">2023-10-10</text>
                    <text>删除</text>
                </view>
                <view class="color-#666 flex flex-col">
                    <text class="comment">我的评价内容(文本+图片)</text>
                    <view class="flex justify-around">
                        <image
                            class="w-25 h-20"
                            src="https://ts3.cn.mm.bing.net/th?id=OIP-C.YwQZJ_SoLGm-kVT-e-Xc2AHaEo&w=316&h=197&c=8&rs=1&qlt=90&o=6&pid=3.1&rm=2" 
                            mode="aspectFit|aspectFill|widthFix"
                        />
                        <image
                            class="w-25 h-20"
                            src="https://ts3.cn.mm.bing.net/th?id=OIP-C.YwQZJ_SoLGm-kVT-e-Xc2AHaEo&w=316&h=197&c=8&rs=1&qlt=90&o=6&pid=3.1&rm=2" 
                            mode="aspectFit|aspectFill|widthFix"
                        />
                        <image
                            class="w-25 h-20"
                            src="https://ts3.cn.mm.bing.net/th?id=OIP-C.YwQZJ_SoLGm-kVT-e-Xc2AHaEo&w=316&h=197&c=8&rs=1&qlt=90&o=6&pid=3.1&rm=2" 
                            mode="aspectFit|aspectFill|widthFix"
                        />
                    </view>
                </view>
                <u-divider text="" hairline></u-divider>
                <view class="flex gap-3 pb-5">
                    <image
                        class="w-25 h-15"
                        src="https://ts3.cn.mm.bing.net/th?id=OIP-C.YwQZJ_SoLGm-kVT-e-Xc2AHaEo&w=316&h=197&c=8&rs=1&qlt=90&o=6&pid=3.1&rm=2" 
                        mode="aspectFit|aspectFill|widthFix"
                    />
                    <text class="text-3">原创宋制汉服女重工刺绣长衫精子齐腰褶裙春夏款</text>
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