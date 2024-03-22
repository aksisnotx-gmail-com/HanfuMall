<script setup>
    const comment = ref('')
    const fileList3 = [{
        url: 'https://cdn.uviewui.com/uview/swiper/1.jpg',
    }]

    // 文件读取完毕的函数
    const afterRead = (file, lists, name) => {

    }

    const deletePic = (e) => {
        console.log(e, 'e');
    }

    const uToastRef = ref(null)
    const onPublish = () => {
        uToastRef.value.show({
            type: 'success',
            message: '发布成功, 即将返回首页',
            icon: 'https://cdn.uviewui.com/uview/demo/toast/success.png',
            complete() {
               uni.switchTab({
                    url: '/pages/home/index'
                })
            }
        })
    }
</script>

<template>
    <view>
        <u--textarea 
            count
            v-model="comment" 
            placeholder="请输入文本" 
            border="bottom"
        ></u--textarea>

        <view class="h-5"></view>

        <u-upload
            previewFullImage
            :fileList="fileList3"
            @afterRead="afterRead"
            @delete="deletePic"
            name="1"
            multiple
            :maxCount="10"
        ></u-upload>

        <view class="absolute bottom-0 left-0 w-100%">
            <button class="bg-#558AE6 color-#fff font-500" @click="onPublish">发布图文</button>
        </view>

        <u-toast ref="uToastRef"></u-toast>
    </view>
</template>

<style scoped>
    :deep(.u-upload__deletable) {
        width: 24px!important;
        height: 24px!important;
    }

    :deep(.u-icon__icon) {
        font-size: 20px!important;
        line-height: 20px!important;
    }
</style>