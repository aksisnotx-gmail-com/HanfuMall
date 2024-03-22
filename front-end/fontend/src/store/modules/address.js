import { defineStore } from "pinia";

export const useAddressStore = defineStore("address", {
    state: () => ({
        addressList: [
            {
                id: 1,
                name: '张三',
                address: '北京市东城区银杏街道',
                area: '北京市东城区',
                detailAddress: '银杏街道',
                phone: '13812345678'
            },
            {
                id: 2,
                name: '李四',
                address: '天津市和平区印象街道',
                area: '天津市和平区',
                detailAddress: '印象街道',
                phone: '138987654321'
            },
        ],
        curAddress: {},
        editAddress: {}
    }),
    actions: {
        setCurAddress (addressId) {
            const address = this.addressList.find(item => item.id === addressId)
            this.curAddress = { ...address }
        }
    }
})