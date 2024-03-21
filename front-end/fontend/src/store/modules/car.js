import { defineStore } from "pinia";

export const useCarStore = defineStore("car", {
    state: () => ({
        goodsList: [],
        totalPrice: 0
    }),
    actions: {
        addToCar(goods, price) {
            this.goodsList = [ ...goods ]
            this.totalPrice = price;
        }
    }
})