<script setup>
	import { useGoodsStore } from '@/store/modules/goods'
	import { getAllProductApi, getProductBySearchApi } from '@/api/home'

	const pageInfo = reactive({
        current: 1,
        size: 20,
        total: 0
    })

	const searchStyle = {
		width: '120rpx',
		color: '#fff',
		backgroundColor: '#7DA1DC',
		height: '70rpx',
		display: 'flex',
		alignItems: 'center',
		justifyContent: 'center',
		borderRadius: '20rpx'
	}
	const keyword = ref('')

	const onSearch = () => {
		const val = keyword.value.trim()
		if(!val) return

		getProductBySearch(val)
	}

	const onClear = () => {
		keyword.value = ''
		const len = productList.value.length
		productList.value.splice(0, len)
		getAllProduct(pageInfo.current)
	}

	const siftShow = ref(true)
	const priceSift = () => {
		siftShow.value = !siftShow.value
		if(siftShow.value) {
			getAllProduct(pageInfo.current)
		} else {
			productList.value.forEach(item => {
				item.specCombinationList.sort((a, b) => {
					const priceA = !a.specialPrice ? a.specialPrice : a.price
					const priceB = !b.specialPrice ? b.specialPrice : b.price
				    return priceB - priceA
				})
			})
		}
	}

	
	const goodsStore = useGoodsStore()
	const onJumpDetail = (productId) => {
		goodsStore.productId = productId

		uni.navigateTo({
            url: '/pagesA/pages/goodsItem/index'
        })
	}


	const productList = ref([])
	const getAllProduct = async (currentt = 1) => {
		const res = await getAllProductApi(currentt)
		const { records, current, size, total } = res.data
		const len = records.length
		if(len) {
			pageInfo.current = current
            pageInfo.size = size
            pageInfo.total = total
			productList.value = [ ...records ]
		}
	}

	const getProductBySearch = async (productName) => {
		const proLen = productList.value.length
		productList.value.splice(0, proLen)
		const res = await getProductBySearchApi(productName)
		const { records } = res.data
		const len = records.length
		if(len) {
			productList.value = [ ...records ]
		}
	}

	onMounted(() => {
		getAllProduct()
	})

	onReachBottom(async () => {		
		uni.showLoading({
            title: '加载中'
        });
		const currentTotal = pageInfo.current * pageInfo.size
		if(currentTotal < pageInfo.total) {
            pageInfo.current++
            await getAllProduct(pageInfo.current)
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
</script>

<template>
	<view class="bg-#F4F4F4 pb-6 viewport">
		<view
			class="fixed top-0 right-0 left-0 "
		>
			<view class="bg-#fff p-3">
				<u-search
					clearabled
					placeholder="日照香炉生紫烟" 
					:value="keyword"
					:actionStyle="searchStyle"
					@search="onSearch"
					@custom="onSearch"
					@clear="onClear"
				></u-search>

				<template v-if="siftShow">
					<view class="h-8 flex items-end gap-6">
						<u-icon 
							name="arrow-down-fill" 
							color="#EB0101" 
							size="20" 
							label="综合"
							labelSize="16"
							labelColor="#EB0101"
							labelPos="left"
							space="1"
						></u-icon>
						<text @click="priceSift">价格</text>
					</view>
				</template>
				<template v-else>
					<view class="h-8 flex items-end gap-11.25">
						<text @click="priceSift">综合</text>
						<u-icon 
							name="arrow-down-fill" 
							color="#EB0101" 
							size="20" 
							label="价格"
							labelSize="16"
							labelColor="#EB0101"
							labelPos="left"
							space="1"
						></u-icon>
					</view>
				</template>
			</view>
		</view>
		<template v-if="!productList.length">
			<view class="mt-22.75">
				<u-empty
					text="暂无商品数据"
					mode="data"
				>
				</u-empty>
			</view>
		</template>
		<template v-else>
			<view class="flex justify-between flex-wrap px-3 mt-22.75">
				<template v-for="item of productList" :key="item.id">
					<template v-if="!item.specCombinationList.length">
						<u-empty
							text="暂无商品数据"
							mode="data"
						>
						</u-empty>
					</template>
					<template v-else>
						<template v-for="product of item.specCombinationList" :key="product.id">
							<view 
								class="item_card"
								@click="onJumpDetail(item.id)"
							>
								<view class="flex flex-col py-3 px-2">
									<image
										:src="product.carouselUrl"
										mode="aspectFit"
										class="w-100% h-25"
									/>
									<text class="mb-2">{{ product.desc }}</text>
									<text class="color-#DC143C font-600">¥ {{ product.specialPrice ? product.specialPrice : product.price }}</text>
								</view>
							</view>
						</template>
					</template>
				</template>
			</view>
		</template>
	</view>
</template>

<style scoped>
	.viewport {
		min-height: calc(100vh - 91px);
	}

	.item_card {
		margin-top: 12px;
		width: 48%;
		border-radius: 20px;
		background: #fff;
		box-shadow: 20px 20px 60px #bebebe,
					-20px -20px 60px #ffffff;
    }
</style>
