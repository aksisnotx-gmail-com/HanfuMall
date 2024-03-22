<script setup>
    import citySelect from './components/citySelect.vue';
    import { useAddressStore } from '@/store/modules/address'

    const addressStore = useAddressStore()

    const data = reactive({
        isShow: false,
        input: ''
    })

    const addressInfo = reactive({
        name: '',
        phone: '',
        area: '',
        address: ''
    })

    const initAddress = () => {
        if(!addressStore.editAddress?.id) return

        addressInfo.name = addressStore.editAddress.name
        addressInfo.phone = addressStore.editAddress.phone
        addressInfo.area = addressStore.editAddress.area
        addressInfo.address = addressStore.editAddress.detailAddress
    }


    const rules = reactive({
        'name': {
            type: 'string',
            required: true,
            message: '请输入姓名',
            trigger: ['input', 'blur']
        },
        'phone': {
            type: 'number',
            required: true,
            message: '请输入手机号',
            trigger: ['input', 'blur'],
            len: 11
        },
        'area': {
            type: 'string',
            required: true,
            message: '请选择地区',
            trigger: ['input', 'blur']
        },
        'address': {
            type: 'string',
            required: true,
            message: '请输入详细地址',
            trigger: ['input', 'blur'],
            min: 6,
            max: 20
        }
    })

    const chooiseArea = () => {
        data.value = true
    }

    const cityChange = (e) => {
        data.input = e.province.label + '' + e.city.label + '' + e.area.label;
        addressInfo.area = data.input
    }

    const addressForm = ref(null)
    const onSaveAddress = () => {
        addressForm.value.validate().then(valid => {
            if(valid) {
                const len = addressStore.addressList.length
                addressStore.addressList.push({
                    ...addressInfo,
                    id: len + 1, 
                    address: addressInfo.area + addressInfo.address,
                    detailAddress: addressInfo.address
                })

                uni.navigateBack()
            }
        })
    }

    onShow(() => {
        initAddress()
    })
</script>

<template>
    <view>
        <view class="pl-3">
            <u--form
				labelPosition="left"
				:model="addressInfo"
				ref="addressForm"
                :rules="rules"
                >
                <u-form-item
                        label="收件人: "
                        prop="name"
                        ref="nameRef"
                        labelWidth="69"
                >
                    <u--input
                            v-model="addressInfo.name"
                            placeholder="请选择收件人"
                            border="bottom"
                    ></u--input>
                </u-form-item>
                <u-form-item
                        type="number"
                        label="联系电话: "
                        prop="phone"
                        ref="phoneRef"
                        labelWidth="69"
                >
                    <u--input
                            v-model="addressInfo.phone"
                            placeholder="请选择联系电话"
                            border="bottom"
                    ></u--input>
                </u-form-item>
                <u-form-item
                        label="省市区: "
                        prop="area"
                        labelWidth="69"
                        @click="chooiseArea"
                >
                        <u--input
                            v-model="addressInfo.area"
                            placeholder="请选择省、市、区"
                            disabled
                            disabledColor="#fff"
                            border="bottom"
                        ></u--input>
                    <template #right>
                        <u-icon
                            name="arrow-right"
                        ></u-icon>
                    </template>
                </u-form-item>
                <u-form-item
                        type="address"
                        label="详细地址: "
                        prop="address"
                        ref="addressRef"
                        labelWidth="69"
                >
                    <u--input
                            v-model="addressInfo.address"
                            placeholder="请输入详细地址"
                            border="bottom"
                    ></u--input>
                </u-form-item>
            </u--form>
        </view>

        <view class="save" @click="onSaveAddress">
            保存
		</view>
        <citySelect v-model="data.value" @city-change="cityChange"></citySelect>
    </view>

</template>

<style scoped>
    .save {
		width: calc(100% - 24px);
        height: 40px;
        background-color: #7DA1DC;
		position: fixed;
		bottom: 30px;
        left: 12px;
        right: 12px;
		display: flex;
		align-items: center;
		justify-content: space-around;
        border-radius: 20px;
        color: #fff;
        font-size: 18px;
        letter-spacing: 2.57rpx;
    }
</style>