<script setup>
	const keyword = ref('')

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

	const reachBottom = ref(false)
	onReachBottom(() => {
		reachBottom.value = true
	})
</script>

<template>
	<view class="bg-#F4F4F4 pb-6">
		<view class="bg-#fff p-3">
			<u-search
				clearabled
				placeholder="日照香炉生紫烟" 
				v-model="keyword"
				:actionStyle="searchStyle"
			></u-search>
			  
			<view class="h-8 flex items-end gap-10">
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
				<text>价格</text>
			</view>
		</view>
		<view class="flex justify-between flex-wrap px-3">
			<block v-for="item of 6" :key="item">
				<view class="item_card">
					<view class="flex flex-col py-3 px-2">
						<image
							src="https://cdn.uviewui.com/uview/album/1.jpg"
							mode="aspectFit"
							class="w-100% h-25"
						/>
						<text class="mb-2">明制剪花流苏圆领短比甲</text>
						<text class="color-#DC143C font-600">¥ 19.90</text>
					</view>
				</view>
			</block>
		</view>

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
	.item_card {
		margin-top: 12px;
		width: 48%;
		border-radius: 20px;
		background: #fff;
		box-shadow: 20px 20px 60px #bebebe,
					-20px -20px 60px #ffffff;
    }
</style>
