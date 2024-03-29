import { defineStore } from "pinia";

import { getMsgApi } from '@/api/tabbar/my'

export const useMsgStore = defineStore("msg", {
    state: () => ({
        unReadComments: [],
        unReadLikes: [],
    }),
    actions: {
        // 添加未读消息
        async addUnReadinfo() {
            const res = await getMsgApi()
            const { code, data } = res
            if(code == 200) {
                this.unReadLikes = [ ...data.unReadLikes ]
                this.unReadComments = [ ...data.unReadComments.comment, ...data.unReadComments.reply ]
            }
        }
    }
})