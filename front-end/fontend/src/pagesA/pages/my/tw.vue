<script setup>
    const urls2 = [
        'https://cdn.uviewui.com/uview/album/1.jpg',
        'https://cdn.uviewui.com/uview/album/2.jpg',
        'https://cdn.uviewui.com/uview/album/3.jpg',
        'https://cdn.uviewui.com/uview/album/4.jpg',
        'https://cdn.uviewui.com/uview/album/5.jpg',
        'https://cdn.uviewui.com/uview/album/6.jpg',
        'https://cdn.uviewui.com/uview/album/7.jpg',
        'https://cdn.uviewui.com/uview/album/8.jpg',
        'https://cdn.uviewui.com/uview/album/9.jpg',
        'https://cdn.uviewui.com/uview/album/10.jpg',
    ]


    const editShow = ref(false)

    const onEdit = () => {
        editShow.value = true
    }

    const list = ref([
        {
            name:'删除',
            color:'#ffaa7f',
            fontSize:'20'
        },
        {
            name:'取消',
            color:'#ccc',
            fontSize:'20'
        },
    ])

    const onSelect = (e) => {
        const { name } = e
        console.log(name, 'name');
    }
</script>

<template>
    <view class="bg-#f2f2f2 pb-4">
        <view>
            <view class="bg-#fff mb-3">
                <view class="u-demo-block">
                    <view class="u-demo-block__content">
                        <view class="album">
                            <view class="album__content px-3 flex flex-col">
                                <view 
                                    class="flex justify-between items-center h-10"
                                    @click="onEdit"
                                >
                                    <text class="color-#666">2023/3/2</text>
                                    <u-icon 
                                        name="arrow-down" 
                                        size="22"
                                    ></u-icon>
                                </view>
                                <text class="color-#666">汉家衣裳，渊源流长。衣冠上国，礼义之邦。</text>
                                <u-album 
                                    :urls="urls2"
                                    multipleSize="110"
                                ></u-album>
                            </view>
                        </view>
                    </view>
                </view>
                <u-divider text="" hairline></u-divider>
                <view class="h-8 flex justify-end px-10 gap-6">
                    <u-icon 
                        name="thumb-up" 
                        size="24"
                        label="118"
                        space="1"
                    ></u-icon>
                    <u-icon 
                        name="chat" 
                        size="24"
                        label="18"
                        space="1"
                    ></u-icon>
                    <u-icon 
                        name="trash" 
                        size="24"
                        label=""
                        space="1"
                    ></u-icon>
                </view>
            </view>

            <view class="bg-#fff mb-3">
                <view class="u-demo-block">
                    <view class="u-demo-block__content">
                        <view class="album">
                            <view class="album__content px-3 flex flex-col">
                                <view 
                                    class="flex justify-between items-center h-10"
                                    @click="onEdit"
                                >
                                    <text class="color-#666">2023/3/2</text>
                                    <u-icon 
                                        name="arrow-down" 
                                        size="22"
                                    ></u-icon>
                                </view>
                                <text class="color-#666">汉家衣裳，渊源流长。衣冠上国，礼义之邦。</text>
                                <u-album 
                                    :urls="urls2"
                                    multipleSize="110"
                                ></u-album>
                            </view>
                        </view>
                    </view>
                </view>
                <u-divider text="" hairline></u-divider>
                <view class="h-8 flex justify-end px-10 gap-10">
                    <u-icon 
                        name="thumb-up" 
                        size="24"
                        label="118"
                        space="1"
                    ></u-icon>
                    <u-icon 
                        name="chat" 
                        size="24"
                        label="18"
                        space="1"
                    ></u-icon>
                </view>
            </view>
        </view>

        <u-action-sheet
            closeOnClickOverlay
            closeOnClickAction
            :actions="list" 
            :show="editShow"
            @close="editShow = false"
            @select="onSelect"
        ></u-action-sheet>
    </view>
</template>

<style scoped lang="scss">
    .album {
        @include flex;
        align-items: flex-start;

        &__avatar {
             padding: 5px;
             border-radius: 3px;
             margin-left: 5px;
         }
    
        &__content {
            flex: 1;
            margin-left: 5px;
         }
    }
</style>