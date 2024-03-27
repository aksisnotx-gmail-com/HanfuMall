import { defineStore } from "pinia";

export const useCommentStore = defineStore("comment", {
    state: () => ({
        discoveryId: ''
    })
})