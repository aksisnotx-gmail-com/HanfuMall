<script setup>
    import env from '@/utils/config';
    import { uploadImgApi } from '@/api/file/index.js'

    const comment = ref('')
    const fileList = ref([
        {
            url: 'https://cdn.uviewui.com/uview/swiper/1.jpg'
        },
        {
            url: 'https://cdn.uviewui.com/uview/swiper/1.jpg',
        }
    ])

    // 文件读取完毕的函数
    const afterRead = (event) => {
        uni.uploadFile({
            header: {
                'Content-Type': 'multipart/form-data' // 请求体的编码格式
            },
            url: env + 'file/upload', // 后端接口文档上的接口地址
            filePath: event.file.url, // 图片的路径
            name: 'file',
            file: event.file, // 文件对象
            // 上传成功回调
            success: function (res) {
                const data = JSON.parse(res.data)
                console.log(data)
                // 上传成功之后拿到 res ，然后进行你的下一步操作
            },
            // 上传失败回调
            fail: function (err) {
                console.log(err)
            }
        })
    }

    const deletePic = (e) => {
        const { index } = e
        fileList.value.splice(index, 1)
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
            :fileList="fileList"
            name="upload-img"
            accept="image"
            :maxCount="9"
            @afterRead="afterRead"
            @delete="deletePic"
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