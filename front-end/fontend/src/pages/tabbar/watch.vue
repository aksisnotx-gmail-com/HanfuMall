<script setup>
    const albumWidth = ref(0)

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

    const JumpDetail = () => {
        uni.navigateTo({
            url: '/pagesA/pages/wDetail/index'
        })
    }
</script>

<template>
    <view class="bg-#f4f4f4">
        <block v-for="item of 10" :key="item">
            <view class="bg-#fff mb-3" @click="JumpDetail">
                <view class="u-demo-block">
                    <view class="u-demo-block__content">
                        <view class="album">
                            <view class="album__avatar">
                                <image
                                src="https://ijry.github.io/uview-plus/h5/assets/logo-8d54bbeb.png"
                                mode="aspectFit"
                                style="width: 32px;height: 32px;"
                                ></image>
                            </view>
                            <view class="album__content">
                                <u--text
                                text="uview-plus UI"
                                type="primary"
                                bold
                                size="17"
                                ></u--text>
                                <u--text
                                margin="0 0 8px 0"
                                text="全面的组件和便捷的工具会让您信手拈来，如鱼得水"
                                ></u--text>
                                <u-album :urls="urls2"></u-album>
                            </view>
                        </view>
                    </view>
                </view>
                <u-divider text="" :hairline="true"></u-divider>
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
        </block>
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