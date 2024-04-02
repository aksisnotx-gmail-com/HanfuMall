<script setup>
    import env from '@/utils/config';
    import { updateApi, getUserApi } from '@/api/auth'
    import CitySelect from '../address/components/citySelect.vue'

    const userInfo = reactive({
        id: '',
        avatar: '',
        nickname: '',
        coordinate: ''
    })

    const rules = reactive({
        'nickname': {
            type: 'string',
            required: true,
            message: '请输入姓名',
            trigger: ['input', 'blur']
        },
        'coordinate': {
            type: 'string',
            required: true,
            message: '请选择地区',
            trigger: ['input', 'blur']
        }
    })

    const labelStyle = {
        'fontWeight': 'bold',
    }

    const areaValue = reactive({
        input: '',
        value: false
    })

    const cityChange = (e) => {
        areaValue.input = e.province.label + '' + e.city.label + '' + e.area.label;
        userInfo.coordinate = areaValue.input
    }

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
                userInfo.avatar = data.message

                uni.setStorageSync('avatar', data.message)
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
        userInfo.avatar = ''
    }


    const userInfoRef = ref(null)
    const onUpdate = () => {
        userInfoRef.value.validate().then(valid => {
            if(valid) {
                if(checkParams(userInfo)) {
                    updateApi(userInfo).then(res => {
                        uni.showToast({
                            title: '修改成功',
                            icon: 'success',
                            mask: true,
                            duration: 3000,
                            success() {
                                uni.switchTab({
                                    url: '/pages/tabbar/my'
                                })
                            }
                        })
                        

                    }).catch(err => {
                        uni.showToast({
                            title: '修改失败',
                            icon: 'error',
                            mask: true,
                            duration: 3000
                        })
                    })
                }
            }
        })
    }

    const userData = ref({})
    const getUserData = async () => {
        const res = await getUserApi()
        userData.value = { ...res.data }
    }

    onLoad(() => {
        getUserData()
    })

    onMounted(() => {
        const { 
            id,
            nickname,
            avatar,
            coordinate,
        } = userData.value

        userInfo.id = id
        userInfo.nickname = nickname
        userInfo.avatar = avatar
        userInfo.coordinate = coordinate
        fileList.value.push({
            url: avatar
        })
    })

    function checkParams(params) {
        // 属性名与提示信息的映射
        const fields = {
            avatar: '头像为空',
            nickname: '昵称为空',
            coordinate: '坐标为空'
        };

        // 遍历映射并检查每个属性
        for (const [key, message] of Object.entries(fields)) {
            if (!params[key]) {
                uni.showToast({
                    title: message,
                    icon: 'error',
                    mask: true
                })
                return false
            }
        }
        
        return true;
    }
</script>

<template>
    <view class="h-80vh layout-abs-center">
        <view class="flex flex-col items-center gap-8">
            <u-upload
                previewFullImage
                :fileList="fileList"
                name="upload-img"
                accept="image"
                :maxCount="1"
                @afterRead="afterRead"
                @delete="deletePic"
            ></u-upload>

            <u--form
				labelPosition="left"
                :labelStyle="labelStyle"
				:model="userInfo"
				:rules="rules"
				ref="userInfoRef"
		    >
            <u-form-item
                label="昵称: "
                prop="nickname"
                labelWidth="40"
                ref="item1"
            >
                <u--input
                    v-model="userInfo.nickname"
                    placeholder="请输入昵称"
                    border="bottom"
                ></u--input>
            </u-form-item>
            <u-form-item
                label="坐标: "
                prop="coordinate"
                labelWidth="40"
                @click="areaValue.value = true"
                >
                    <u--input
                        v-model="userInfo.coordinate"
                        placeholder="请选择坐标"
                        disabled
                        disabledColor="#fff"
                        border="bottom"
                    ></u--input>
                </u-form-item>
            </u--form>
        </view>

        <view class="add" @click="onUpdate">
            提交修改
		</view>

        <citySelect v-model="areaValue.value" @city-change="cityChange"></citySelect>
    </view>
</template>

<style scoped>
    .add {
        padding: 6px 80px;
        height: 40px;
        background-color: #7DA1DC;
		position: fixed;
		bottom: 200px;
		display: flex;
		align-items: center;
		justify-content: space-around;
        border-radius: 40px;
        color: #fff;
        font-size: 18px;
        letter-spacing: 2.57rpx;
    }
</style>