<script setup>
    import CitySelect from '../address/components/citySelect.vue'

    const src = ref('https://ts1.cn.mm.bing.net/th?id=OIP-C.Dfp0b5Jqn7ZFnnlz0zeJQAAAAA&w=250&h=250&c=8&rs=1&qlt=90&o=6&pid=3.1&rm=2')

    const userInfo = reactive({
        name: '',
        area: ''
    })

    const rules = reactive({
        'name': {
            type: 'string',
            required: true,
            message: '请输入姓名',
            trigger: ['input', 'blur']
        },
        'area': {
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
        userInfo.area = areaValue.input
    }

    const userInfoRef = ref(null)
    const onUpdate = () => {
        userInfoRef.value.validate().then(valid => {
            if(valid) {
                console.log(userInfo, 'u');
            }
        })
    }
</script>

<template>
    <view class="h-80vh layout-abs-center">
        <view class="flex flex-col items-center gap-8">
            <up-avatar 
            :src="src"
            size="100"
            ></up-avatar>
            <u--form
				labelPosition="left"
                :labelStyle="labelStyle"
				:model="userInfo"
				:rules="rules"
				ref="userInfoRef"
		    >
            <u-form-item
                label="昵称: "
                prop="name"
                labelWidth="40"
                ref="item1"
            >
                <u--input
                    v-model="userInfo.name"
                    placeholder="请输入昵称"
                    border="bottom"
                ></u--input>
            </u-form-item>
            <u-form-item
                label="坐标: "
                prop="area"
                labelWidth="40"
                @click="areaValue.value = true"
                >
                    <u--input
                        v-model="userInfo.area"
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