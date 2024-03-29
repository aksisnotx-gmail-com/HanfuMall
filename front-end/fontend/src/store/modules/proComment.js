import { defineStore } from 'pinia'

import { getCommentById } from '@/api/tabbar/order'

export const useProCommentStore = defineStore('proComment', {
    state: () => ({
        commentList: [],
        oneComment: {}, // 首个评论
        pager: {
            current: 1,
            size: 20,
            total: 0
        }
    }),
    actions: {
        async getCommentListById (productId, currentt = 1) {
            const res = await getCommentById(productId, currentt)
            const { total, size, current, records} = res.data
            this.pager.current = current
            this.pager.size = size
            this.pager.total = total
            this.commentList = [ ...records ]

            this.oneComment = records[0] || {}
        }
    }
})