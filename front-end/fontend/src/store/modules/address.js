import { defineStore } from "pinia";
import { updateApi, getUserApi } from '@/api/auth'

export const useAddressStore = defineStore("address", {
    state: () => ({
        addressList: [],
        shippingAddress: [], // json字符串地址
        curAddress: {},
        editAddress: {}
    }),
    actions: {
        async getAddressList () {
            const res = await getUserApi()
            const { code, data } = res
            if(code === 200) {
                uni.setStorageSync('id', data.id)
                uni.setStorageSync('coordinate', data.coordinate)
                if(data.shippingAddress) {
                    this.shippingAddress = [ ...data.shippingAddress ]
                    this.addressList = data.shippingAddress.map(item => JSON.parse(item)) || []
                }
            }
        },
        async updateAddress (paramsjSON) {
            const id = uni.getStorageSync('id')
            const nickname = uni.getStorageSync('nickname')
            const avatar = uni.getStorageSync('avatar')
            const coordinate = uni.getStorageSync('coordinate')

            const params = {
                id,
                nickname,
                avatar,
                coordinate,
                shippingAddress: paramsjSON
            }


            const res = await updateApi(params)
            const { code, data } = res
            if(code === 200) {
                this.shippingAddress = [ ...data.shippingAddress ]
                this.addressList = data.shippingAddress.map(item => JSON.parse(item)) || []
                uni.showToast({
                    title: '修改成功',
                    icon: 'success',
                    duration: 3000
                })
            }
        },
        async delAddress (shippingAddress) {
            const id = uni.getStorageSync('id')
            const nickname = uni.getStorageSync('nickname')
            const avatar = uni.getStorageSync('avatar')
            const coordinate = uni.getStorageSync('coordinate')

            const params = {
                id,
                nickname,
                avatar,
                coordinate,
                shippingAddress
            }
            const res = await updateApi(params)
            const { code, data } = res
            if(code === 200) {
                this.shippingAddress = [ ...data.shippingAddress ]
                this.addressList = data.shippingAddress.map(item => JSON.parse(item)) || []
                uni.showToast({
                    title: '删除成功',
                    icon: 'success',
                    duration: 3000
                })
            }
        },
        setCurAddress (addressId) {
            const address = this.addressList.find(item => item.id === addressId)
            this.curAddress = { ...address }
        }
    }
})
