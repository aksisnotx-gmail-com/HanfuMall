<script setup>
    const rate = ref(4)

    const color = ref('pink')
</script>

<template>
    <view class="bg-#F2F2F2 p-3">
        <template v-for="item of 4" :key="item">
            <view class="item_card py-3 mb-3">
                <view class="flex max-h-80 overflow-hidden p-3">
                    <up-avatar 
                        src="https://ts2.cn.mm.bing.net/th?id=OIP-C.54qlbLNAZ64K94c_DCT-qAAAAA&w=166&h=166&c=8&rs=1&qlt=90&o=6&pid=3.1&rm=2" 
                        shape="circle"
                    ></up-avatar>
                    <view class="flex flex-col ml-2">
                        <view class="w-70 flex justify-between">
                            <text class="text-3 color-#999">用户名</text>
                            <text class="text-3 color-#999">2020/1/2</text>
                        </view>
                        <view class="my-1">
                            <u-rate
                                :modelValue="rate"
                            ></u-rate>
                        </view>
                            
                        <view class="ellipsis my-2">
                            《叶圣陶散文》为“名家经典珍藏”丛书之一，收录了叶圣陶先生的散文精品数十篇。这些作品内容丰富，题材各异，构思精巧，文笔精巧、语言幽默、内蕴深厚、风格恬淡，充分显示了叶圣陶先生的文学功底及丰富的人生阅历，从一个侧面反映了作者的思想感情及创作风格，非常值得一读。叶圣陶是20世纪中国一位杰出的作家、教育家和出版家，又是中国现代儿童文学创作的先行者。作为散文家，他早期和周作人、朱自清共同成为文学研究会散文创作的中坚，后来又成为开明派散文的代表，其散文被一九三五年出版的《中国新文学大系》选录的篇数仅次于周作人、鲁迅和朱自清。《叶圣陶散文》为“名家经典珍藏”丛书之一，收录了叶圣陶先生的散文精品数十篇。这些作品内容丰富，题材各异，构思精巧，文笔精巧、语言幽默、内蕴深厚、风格恬淡，充分显示了叶圣陶先生的文学功底及丰富的人生阅历，从一个侧面反映了作者的思想感情及创作风格，非常值得一读。叶圣陶是20世纪中国一位杰出的作家、教育家和出版家，又是中国现代儿童文学创作的先行者。作为散文家，他早期和周作人、朱自清共同成为文学研究会散文创作的中坚，后来又成为开明派散文的代表，其散文被一九三五年出版的《中国新文学大系》选录的篇数仅次于周作人、鲁迅和朱自清。《叶圣陶散文》为“名家经典珍藏”丛书之一，收录了叶圣陶先生的散文精品数十篇。这些作品内容丰富，题材各异，构思精巧，文笔精巧、语言幽默、内蕴深厚、风格恬淡，充分显示了叶圣陶先生的文学功底及丰富的人生阅历，从一个侧面反映了作者的思想感情及创作风格，非常值得一读。叶圣陶是20世纪中国一位杰出的作家、教育家和出版家，又是中国现代儿童文学创作的先行者。作为散文家，他早期和周作人、朱自清共同成为文学研究会散文创作的中坚，后来又成为开明派散文的代表，其散文被一九三五年出版的《中国新文学大系》选录的篇数仅次于周作人、鲁迅和朱自清。
                        </view>
                        <view class="flex gap-3">
                            <up-image show-loading src="https://cdn.uviewui.com/uview/album/1.jpg" width="80px" height="80px"></up-image>
                            <up-image show-loading src="https://cdn.uviewui.com/uview/album/1.jpg" width="80px" height="80px"></up-image>
                            <up-image show-loading src="https://cdn.uviewui.com/uview/album/1.jpg" width="80px" height="80px"></up-image>
                        </view>
                    </view>
                </view>
            </view>
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