import { defineStore } from 'pinia'

export const useGoodsStore = defineStore('goods', {
    state: () => ({
        goodsItem: {}
    })  
})