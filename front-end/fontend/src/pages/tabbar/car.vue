<script setup>
	import { useCarStore } from '@/store/modules/car'
	const carStore = useCarStore()

	const list = ref([])

	const itemGrounpChecked = ref([])

	const modelTitle = ref('管理')
	const onControl = () => {
		modelTitle.value = modelTitle.value === '管理' ? '退出管理' : '管理'
	}

	const delGoods = () => {
		if(!itemGrounpChecked.value.length) {
			uni.showToast({
				title:'请选择商品',
				icon:'none'
			})
			return
		}

		list.value = list.value.filter(item => !itemGrounpChecked.value.includes(item.name))
		itemGrounpChecked.value.splice(0, Infinity)
	}

	const handleCheck = (detail) => {
		console.log(detail, 'detail');
	}

	// 全选
	const isAllChecked = computed({
		get(){
			const listLen = list.value.length
			const checkedLen = itemGrounpChecked.value.length
			if(!listLen) return []
			if(listLen === checkedLen) return ['all']
		},
		// 全选---->list列表
		set(val){
			if(val.length) {
				itemGrounpChecked.value = list.value.map(item => item.name)
			} else {
				itemGrounpChecked.value.splice(0, Infinity)
			}
		}
	})

	// 购物车商品总价
	const cartTotalPrice = computed(() => {
		const checkedItemSet = new Set(itemGrounpChecked.value)
		return list.value.reduce((prev, cur) => {
			if(checkedItemSet.has(cur.name)) {
				prev += cur.sumPrice
			}
			return prev
		}, 0)
	})
 
	// 增加商品数量
	const add = (id) => {
		list.value.forEach(el => {
		if(el.id == id){
			if(el.count < el.stock){
				el.count++
				// 商品小计价格也要改变
				el.sumPrice=el.count*el.price
			}else{
				uni.showToast({
					title:'库存不足哦~',
					icon:'none'
				})
			}
		}
	})
	}

	// 减少商品数量
	const reduce = (id) => {
		list.value.forEach( el => {
		if(el.id == id){
			if(el.count > 1){
				el.count--
				// 商品小计价格也要改变
				el.sumPrice = el.count*el.price
			}else{
				uni.showToast({
					title:'至少购买一件商品哦',
					icon:'none'
				})
			}
		}
	})
	}
	
	// 提交购物车订单
	const submitOrder = () => {
		if(!itemGrounpChecked.value.length) {
			uni.showToast({
				title:'请选择商品',
				icon:'none'
			})
			return
		}

		const checkedItemSet = new Set(itemGrounpChecked.value)
		const carList = list.value.filter(el => checkedItemSet.has(el.name))
		carStore.addToCar(carList, cartTotalPrice.value)

		uni.navigateTo({
			url: '/pagesA/pages/balance/index'
		})
	}

	onMounted(() => {
		// 模拟从后台拿到的数据
		var orginList=[
				{
					id:'0',
					name:'西瓜红红薯',
					price:20,
					count:1,
					size:'小',
					sumPrice:20,
					stock:22,
					img:'https://ts4.cn.mm.bing.net/th?id=OIP-C.mAVGJgwFDE7fhvAYn1qslAHaHa&w=250&h=250&c=8&rs=1&qlt=90&o=6&pid=3.1&rm=2'
				},
				{
					id:'1',
					name:'南瓜',
					price:10,
					count:3,
					size:'中等',
					sumPrice:30,
					stock:10,
					img:'https://ts1.cn.mm.bing.net/th?id=OIP-C.CpUdnoVqBYS5qngcMAaK3AHaE8&w=306&h=204&c=8&rs=1&qlt=90&o=6&pid=3.1&rm=2'
				},
				{
					id:'2',
					name:'红枣',
					price:10,
					count:4,
					size:'迷你',
					sumPrice:40,
					stock:11,
					img:'https://ts2.cn.mm.bing.net/th?id=OIP-C.iB6ds4_IqvUWdlmaNYifWQHaHa&w=250&h=250&c=8&rs=1&qlt=90&o=6&pid=3.1&rm=2'
				}
			]
			
			// list数组中为每一项添加双向绑定的属性---这个属性要在页面显示(onShow)添加
			// orginList.forEach(el => el.isChecked = false);
			
			list.value = orginList;
	})
</script>

<template>
	<view class="cart">
		<view class="flex justify-end items-center bg-#fff h-14 mb-2.5">
			<text 
				class="bg-#7DA1DC color-#fff px-5 py-2 mr-3 rd-1.5" 
				@click="onControl"
			>{{ modelTitle }}</text>
		</view>

		<view class="px-2">
			<u-checkbox-group v-model="itemGrounpChecked" @change="handleCheck">
				<view class="list" v-for="item in list" :key='item.id'>
					<view class="l">
						<!-- 列表的复选框 -->
						<u-checkbox
						:name="item.name"
						shape="circle" 
						activeColor="#7DA1DC"
					></u-checkbox>
					<image :src="item.img" mode="aspectFit" class="img"></image>
					</view>
					<view class="center">
						<view class="name">
							{{item.name}}
						</view>
						<view class="size">
							尺寸: {{item.size}}
						</view>
						<view class="count">
							数量: x{{item.count}}
						</view>
					</view>
					<view class="r">
						<!-- 商品小计 -->
						<view class="price">
							<!-- ￥{{item.price*item.count}}元 -->
							￥{{item.sumPrice}}元
							
						</view>
						<view class="update-count">
							<view class="reduce" @tap="reduce(item.id)">
							-
							</view>
							<view class="cart-count">
								{{item.count}}
							</view>
							<view class="add" @tap="add(item.id)">
								+
							</view>
						</view>
					</view>
				</view>
			</u-checkbox-group>
		</view>
		
		<!-- 底部导航栏 -->
		<view class="tabbar">
			<template v-if="modelTitle === '管理'">
				<view class="all">
					<u-checkbox-group
						v-model="isAllChecked" 	
					>
						<u-checkbox 
							name="all"
							shape="circle"
							activeColor="#7DA1DC" 
							label="全选" 
						></u-checkbox>
					</u-checkbox-group>
				</view>
				<view class="totalPrice">
					总价:￥{{cartTotalPrice}}元
				</view>
				<view class="submitOrder bg-#7DA1DC" @click="submitOrder">
					去结算
				</view>
			</template>
			<template v-else>
				<view class="all">
					<u-checkbox-group
						v-model="isAllChecked" 	
					>
						<u-checkbox 
							name="all"
							shape="circle"
							activeColor="#7DA1DC" 
							label="全选" 
						></u-checkbox>
					</u-checkbox-group>
				</view>
				<view class="submitOrder bg-#DD3938" @click="delGoods">
					删除
				</view>
			</template>
		</view>
	</view>
</template>

<style lang="scss" scoped>
	.cart {
		height: 100vh;
		background: #EEEEEE;
	}

	// 居中显示
	@mixin textCenter {
		display: flex;
		align-items: center;
		justify-content: center;
	}
	.list{
		width: 100%;
		height: 208rpx;
		background: #fff;
		box-shadow: 0 8rpx 16rpx 0 rgba(83,66,49,0.08);
		border-radius: 24rpx;
		border-radius: 24rpx;
		display: flex;
		justify-content: space-around;
		align-items: center;
		margin-bottom: 10px;
		.l{
			display: flex;
			align-items: center;
			.img{
				width: 136rpx;
				height: 136rpx;
				background-color: pink;
				margin-left: 10rpx;
				border-radius: 8%;
			}
		}
		.center{
			width: 170rpx;
			.name{
				font-size: 26rpx;
				color: #3E3E3E;
				letter-spacing: 1.86rpx;
				white-space: nowrap;
				text-overflow: ellipsis;
				overflow: hidden;
			}
			.size{
				font-size: 22rpx;
				color: #8D8D8D;
				letter-spacing: 1.57rpx;
			}
			.count{
				font-size: 22rpx;
				color: #8D8D8D;
				letter-spacing: 1.57rpx;
			}
		}
		.r{
			.price{
				margin-top: 40rpx;
				font-size: 26rpx;
				color: red;
				letter-spacing: 0;
				margin-left: 40rpx;
			}
			// 加减数量
			.update-count{
				margin-top: 40rpx;
				display: flex;
				.reduce{
					width: 40rpx;
					height: 40rpx;
					background: #F1ECE7;
					border-radius: 21.6rpx;
					border-radius: 21.6rpx;
					color: #979797;
				    @include textCenter;
					font-size: 50rpx;
				}
				.add{
					width: 40rpx;
					height: 40rpx;
					background: #F1ECE7;
					border-radius: 21.6rpx;
					border-radius: 21.6rpx;
					color: #979797;
					@include textCenter;
					font-size: 40rpx;
				}
				.cart-count{
					width: 72rpx;
					height: 40rpx;
					background: #F1ECE7;
					border-radius: 21.6rpx;
					border-radius: 21.6rpx;
					margin-left: 18rpx;
					margin-right: 18rpx;
					text-align: center;
					line-height: 40rpx;
				}
			}
		}
		
	}


	// 底部导航
	.tabbar {
		width: 100%;
		height: 176rpx;
		background-color: #fff;
		position: fixed;
		bottom: 0rpx;
		display: flex;
		align-items: center;
		justify-content: space-around;
		border-radius: 8% 8%;
		.all {
			font-size: 32rpx;
			color: #3E3E3E;
			letter-spacing: 2.29rpx;
			display: flex;
			align-items: center;
		}

		.totalPrice {
			font-size: 32rpx;
			color: #3E3E3E;
			letter-spacing: 2.29rpx;
			color:red;
		}

		.submitOrder {
			width: 208rpx;
			height: 80rpx;
			border-radius: 14rpx;
			font-size: 36rpx;
			color: #FFFFFF;
			letter-spacing: 2.57rpx;
			display: flex;
			align-items: center;
			justify-content: center;
		}
	}
</style>
import { onMounted } from '@vue/runtime-core';

