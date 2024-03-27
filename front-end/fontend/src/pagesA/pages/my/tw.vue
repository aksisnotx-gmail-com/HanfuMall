<script setup>
    import { getAllDiscoveryApi, delDiscoveryApi } from '@/api/tabbar/watch'
    import { useCommentStore } from '@/store/modules/comment'

    const pageInfo = reactive({
        current: 1,
        size: 20,
        total: 0
    })

    const discoveryList = ref([])

    const getAllDiscovery = async (currentt = 1) => {
        const res = await getAllDiscoveryApi('MY', currentt)
		const { records, current, size, total } = res.data
		const len = records.length
        if(len) {
            pageInfo.current = current
            pageInfo.size = size
            pageInfo.total = total
            discoveryList.value = [ ...records ]
        }
    }

    const commentStore = useCommentStore()
    const JumpDetail = (id) => {
        commentStore.discoveryId = id
        uni.navigateTo({
            url: '/pagesA/pages/wDetail/index'
        })
    }


    const editShow = ref(false)

    const delId = ref('')
    const onEdit = (id) => {
        editShow.value = true

        delId.value = id
    }

    const list = ref([
        {
            name:'删除',
            color:'#ffaa7f',
            fontSize:'20'
        },
        {
            name:'取消',
            color:'#ccc',
            fontSize:'20'
        },
    ])

    const onSelect = async (e) => {
        const { name } = e
        if(name === '删除') {
            const res = await delDiscoveryApi(delId.value)
            const { data, message } = res
            if(data) {
                uni.showToast({
                    title: '删除成功',
                    icon: 'success',
                    duration: 3000,
                    mask: true
                })
                getAllDiscovery(pageInfo.current)
                editShow.value = false
            } else {
                uni.showToast({
                    title: message,
                    icon: 'error',
                    duration: 3000,
                    mask: true
                })
            }
        }
    }

	onReachBottom(async () => {		
		uni.showLoading({
            title: '加载中'
        });
		const currentTotal = pageInfo.current * pageInfo.size
		if(currentTotal < pageInfo.total) {
            pageInfo.current++
            await getAllDiscovery(pageInfo.current)
            uni.hideLoading()
        } else {
            uni.hideLoading()
            uni.showToast({
                title: '没有更多了',
                icon: 'error',
                mask: true,
                duration: 1000
            })
        }
	})

    onMounted(() => {
        getAllDiscovery()
    })
</script>

<template>
    <view class="min-h-100vh bg-#f2f2f2 pb-4">
        <template v-for="item of discoveryList" :key="item.id">
            <view class="bg-#fff mb-3">
                <view class="u-demo-block">
                    <view class="u-demo-block__content">
                        <view class="album" @click.stop="JumpDetail(item.id)">
                            <view class="album__content px-3 flex flex-col">
                                <view 
                                    class="flex justify-between items-center h-10"
                                    @click.stop="onEdit(item.id)"
                                >
                                    <text class="color-#666">{{ item.createTime }}</text>
                                    <u-icon 
                                        name="arrow-down" 
                                        size="22"
                                    ></u-icon>
                                </view>
                                <text class="color-#666">{{ item.descText }}</text>
                                <u-album 
                                    :urls="item.img"
                                    multipleSize="110"
                                ></u-album>
                            </view>
                        </view>
                    </view>
                </view>
                <u-divider text="" hairline></u-divider>
                <view class="h-8 flex justify-end px-10 gap-6">
                    <u-icon 
                        name="thumb-up" 
                        size="24"
                        :label="item.likes"
                        space="1"
                    ></u-icon>
                    <u-icon 
                        name="chat" 
                        size="24"
                        :label="item.comments.length || 0"
                        space="1"
                        @click.stop="JumpDetail(item.id)"
                    ></u-icon>
                </view>
            </view>
        </template>

        <u-action-sheet
            closeOnClickOverlay
            closeOnClickAction
            :actions="list" 
            :show="editShow"
            @close="editShow = false"
            @select="onSelect"
        ></u-action-sheet>

    </view>
</template>

<style scoped lang="scss">
    .album {
        @include flex;
        align-items: flex-start;

        &__avatar {
             padding: 5px;
             border-radius: 3px;
             margin-left: 5px;
         }
    
        &__content {
            flex: 1;
            margin-left: 5px;
         }
    }
</style>