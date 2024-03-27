<script setup>
    import { useAddressStore } from '@/store/modules/address'

    const addressStore = useAddressStore()
    const itemGrounpChecked = ref([])

    const handleCheck = (detail) => {
        if(!detail.length) return

        addressStore.setCurAddress(detail[0])

        uni.navigateBack()
    }

    const add = () => {
        addressStore.editAddress = {}
        uni.navigateTo({
            url: '/pagesA/pages/address/add'
        })
    }

    const onEdit = (item) => {
        addressStore.editAddress = { ...item }
        uni.navigateTo({
            url: '/pagesA/pages/address/add'
        })
    }

    const onDel = (item) => {
        uni.showModal({
            title: '提示',
            content: '确定删除吗',
            success: function (res) {
                if (res.confirm) {
                    const i = addressStore.addressList.findIndex((val) => val.id === item.id)
                    addressStore.addressList.splice(i, 1)

                    const shippingAddress = addressStore.addressList.map(item => JSON.stringify(item))
                    addressStore.delAddress(shippingAddress)
                } else if (res.cancel) {
                    console.log('用户点击取消');
                }
            }
        });
    }

    onMounted(() => {
        addressStore.getAddressList()
    })
</script>

<template>
    <view class="h-100vh bg-#f4f4f4 px-3">
        <template v-if="!addressStore.addressList.length">
            <view class="h-100vh layout-abs-center">
                <u-empty
                    mode="data"
                >
                </u-empty>
            </view>
        </template>

        <template v-else>
            <u-checkbox-group v-model="itemGrounpChecked" @change="handleCheck">
                <view 
                    class="list" 
                    v-for="item in addressStore.addressList" 
                    :key='item.id'
                >
                        <view class="l">
                            <!-- 列表的复选框 -->
                            <u-checkbox
                            :name="item.id"
                            shape="circle" 
                            activeColor="#7DA1DC"
                            size="20"
                        ></u-checkbox>
                        <view class="ml-3 flex flex-col max-w-58">
                            <view class="flex text-3.5 gap-3">
                                <text>{{ item.name }}</text>
                                <text class="color-#999">{{ item.phone }}</text>
                            </view> 
                            <view>
                                <text class="text-3.5 color-#666">{{ item.address }}</text>
                            </view>
                        </view>
                    </view>
                    <view class="flex">
                        <u-icon name="edit-pen-fill" color="#7DA1DC" size="28" @click="onEdit(item)"></u-icon>
                        <u-icon name="trash-fill" color="#7DA1DC" size="28" @click="onDel(item)"></u-icon>
                    </view>
                </view>
            </u-checkbox-group>
        </template>

        <view class="add" @click="add">
            新增地址
		</view>
    </view>
</template>

<style scoped>
    .list{
        width: 100%;
        padding: 15px 8px;
        background: #fff;
        box-shadow: 0 8rpx 16rpx 0 rgba(83,66,49,0.08);
        border-radius: 24rpx;
        border-radius: 24rpx;
        display: flex;
        justify-content: space-around;
        align-items: center;
        margin-top: 10px;
    }

    .l {
        flex: 1;
        display: flex;
        align-items: center;
        overflow: hidden;
    }

    .add {
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
