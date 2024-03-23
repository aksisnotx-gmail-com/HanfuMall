<script setup>
	import { getAllProductApi, getProductBySearchApi } from '@/api/home'

	const current = ref(0)
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
		productList.value.splice(0, Infinity)
		getAllProduct(current.value)
	}

	const siftShow = ref(true)
	const priceSift = () => {
		siftShow.value = !siftShow.value
		if(siftShow.value) {
			getAllProduct(current.value)
		} else {
			productList.value.sort((a, b) => {
				return b.price - a.price
			})
		}
	}

	const reachBottom = ref(false)
	onReachBottom(() => {		
		current.value++
		getAllProduct(current)
	})

	const productList = ref([])
	const getAllProduct = async (current = 1) => {
		productList.value.length && productList.value.splice(0, Infinity)

		const res = await getAllProductApi(current)
		const records = res.records
		const len = records.length
		if(len) {
			records.forEach(item => {
				if(item.specCombinationList) {
					productList.value = [ ...productList.value, ...item.specCombinationList ]
				}
			})
		} else {
			reachBottom.value = true
		}
	}

	const getProductBySearch = async (productName) => {
		productList.value.splice(0, Infinity)
		const res = await getProductBySearchApi(productName)
	    const records = res.records
		const len = records.length
		if(len) {
			records.forEach(item => {
				if(item.specCombinationList) {
					productList.value = [ ...productList.value, ...item.specCombinationList ]
				}
			})
		}
	}

	onMounted(() => {
		getAllProduct()

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
					<view class="item_card">
						<view class="flex flex-col py-3 px-2">
							<image
								:src="item.carouselUrl"
								mode="aspectFit"
								class="w-100% h-25"
							/>
							<text class="mb-2">{{ item.desc }}</text>
							<text class="color-#DC143C font-600">¥ {{ item.price }}</text>
						</view>
					</view>
				</template>
			</view>
		</template>
		<view 
			v-if="reachBottom"
			class="h-10 px-10"
		>
			<u-divider 
				text="已经到底啦~" 
				hairline
				textColor="#999"
				lineColor="#999"
			></u-divider>
		</view>
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
