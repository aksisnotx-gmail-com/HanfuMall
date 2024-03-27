<script setup>
    import env from '@/utils/config';
    import { addDiscoveryApi } from '@/api/tabbar/watch'

    const comment = ref('')
    const fileList = ref([])

    // 文件读取完毕的函数
    const afterRead = (event) => {
        const token = uni.getStorageSync('token')
        if(!token) {
            uni.showToast({
                title: '登录过期~',
                icon: 'error',
                mask: true,
                duration: 3000
            })
            return
        }
        uni.uploadFile({
            header: {
                token: token
            },
            url: env.fileUrl, // 后端接口文档上的接口地址
            filePath: event.file.url, // 图片的路径
            name: 'file',
            // 上传成功回调
            success: function (res) {
                const data = JSON.parse(res.data)            
                // 上传成功之后拿到 res ，然后进行你的下一步操作
                fileList.value.push({
                    url: data.message
                })
            },
            // 上传失败回调
            fail: function (err) {
                uni.showToast({
                    title: '上传失败',
                    icon: 'error',
                    mask: true,
                    duration: 3000
                })
            }
        })
    }

    const deletePic = (e) => {
        const { index } = e
        fileList.value.splice(index, 1)
    }

    const uToastRef = ref(null)
    const onPublish = async () => {
        const img = fileList.value.map(item => item.url)
        const descText = comment.value.trim()
        if(!descText.length) {
            uni.showToast({
                title: '请输入文本',
                icon: 'error',
                mask: true,
                duration: 3000
            })
            return
        }
        if(!img.length) {
            uni.showToast({
                title: '请上传图片',
                icon: 'error',
                mask: true,
                duration: 3000
            })
            return
        }

        const objParam = {
            descText,
            img
        }
        const res = await addDiscoveryApi(objParam)
        if(res) {
            uToastRef.value.show({
                type: 'success',
                message: '发布成功, 即将返回发现页',
                icon: 'https://cdn.uviewui.com/uview/demo/toast/success.png',
                complete() {
                uni.switchTab({
                        url: '/pages/tabbar/watch'
                    })
                }
            })
        } else {
            fileList.value.splice(0, Infinity)
            comment.value = ''
        }
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