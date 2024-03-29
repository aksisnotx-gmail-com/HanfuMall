<script setup>
    import env from '@/utils/config'
    import { addComment } from '@/api/tabbar/order'

    const rateCount = ref(5)

    const evaluateInfo = reactive({
        value: 0,
        content: ''
    })

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

    const orderId = ref('')
    const publish = async () => {
        const starRating = evaluateInfo.value
        if(!starRating) {
            uni.showToast({
                title: '请选择评分',
                icon: 'error',
                mask: true,
                duration: 2000
            })
            return
        }

        const commentContent = evaluateInfo.content.trim()
        if(!commentContent) {
            uni.showToast({
                title: '请输入评价内容',
                icon: 'error',
                mask: true,
                duration: 2000
            })
            return
        }

        if(!orderId.value) return

        const commentImgUrl = fileList.value.map(item => item.url)
        
        const params = {
            orderId: orderId.value,
            starRating,
            commentContent,
            commentImgUrl
        }

        const res = await addComment(params)
        if(res.data) {
            uni.showToast({
                title: '评价成功',
                icon: 'success',
                mask: true,
                duration: 1000,
                success() {
                    setTimeout(() => {
                        uni.navigateBack()
                    }, 1100)
                }
            })
        }
    }

    onLoad((options) => {
        const { orderid } = options
        if(!orderid) return

        orderId.value = orderid
    })
</script>

<template>
    <view>
        <view class="star-container">
            <text class="star-title">商品评价: </text>
            <u-rate :count="rateCount" v-model="evaluateInfo.value"></u-rate>
        </view>
        <view class="my-3">
            <u--textarea 
                v-model="evaluateInfo.content" 
                placeholder="请输入评价内容" 
                count 
            ></u--textarea>
        </view>
        <view>
            <u-upload
                previewFullImage
                :fileList="fileList"
                name="upload-img"
                accept="image"
                :maxCount="9"
                @afterRead="afterRead"
                @delete="deletePic"
            ></u-upload>
        </view>

        <button class="submit-button" @click="publish">提交评价</button>
    </view>
</template>

<style scoped>
    .star-container {
        display: flex;
        justify-content: space-between;
    }

    .star-title {
        font-size: 16px;
        color: #333;
    }

    .submit-button {
        width: 100%;
        max-width: 400px;
        height: 40px;
        background-color: #007aff;
        color: #fff;
        border-radius: 5px;
        font-size: 16px;
        text-align: center;
        line-height: 40px;
        border: none;
        cursor: pointer;
    }
</style>