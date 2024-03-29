<script setup>
    import { useGoodsStore } from '@/store/modules/goods'
    import { useAddressStore } from '@/store/modules/address'
    import { useProCommentStore } from '@/store/modules/proComment'

    import { addCarApi } from '@/api/tabbar/car'
    import { createOrderApi, orderPayApi } from '@/api/tabbar/order'

    const goodsStore = useGoodsStore()
    const addressStore = useAddressStore()
    const proCommentStore = useProCommentStore()

    const { productInfo, propertiesList, skuData } = storeToRefs(goodsStore)
    const { setPropertiesList } = goodsStore

    const { oneComment } = storeToRefs(proCommentStore)

    const swiperList = ref([])

    const infoShow = ref(false)
    const chooisGoods = () => {
        infoShow.value = true
    }

    const open = () => {}

    const close = () => {
        infoShow.value = false
    }

    const properties = ref([]) // property 列表
    const skuList = ref([]) // sku 列表
    const matrix = ref([]) // 邻接矩阵存储无向图
    const vertexList = ref([]) // 顶点数组
    const selected = ref([]) // 当前已选的 attribute 列表

    const handleClickAttribute = (propertyIndex, attributeIndex) => {
      const attr = properties.value[propertyIndex].attributes[attributeIndex];
      // 若选项置灰，直接返回，表现为点击无响应
      if (attr.isDisabled) {
        return;
      }

      // 重置每个 attribute 的 isActive 状态
      const isActive = !attr.isActive;
      properties.value[propertyIndex].attributes[attributeIndex].isActive = isActive;
      if (isActive) {
        properties.value[propertyIndex].attributes.forEach((attr, index) => {
          if (index !== attributeIndex) {
            attr.isActive = false;
          }
        });
      }

      // 维护当前已选的 attribute 列表
      selected.value.splice(0, selected.value.length);

      properties.value.forEach((prop) => {
        prop.attributes.forEach((attr) => {
          if (attr.isActive) {
            selected.value.push(attr);
          }
        });
      });

      // 重置每个 attribute 的 isDisabled 状态
      properties.value.forEach((prop) => {
        prop.attributes.forEach((attr) => {
          attr.isDisabled = !canAttributeSelect(attr);
        });
      });
    }

    // 构造初始空邻接矩阵存储无向图
    const initEmptyAdjMatrix = () => {
      properties.value.forEach((prop) => {
        prop.attributes.forEach((attr) => {
          vertexList.value.push(attr.value);
        });
      });
      const len = vertexList.value.length
      for (let i = 0; i < len; i++) {
        matrix.value[i] = new Array(len).fill(0);
      }
    }
    
    // 根据 skuList 和 properties 设置邻接矩阵的值
    const setAdjMatrixValue = () => {
      skuList.value.forEach((sku) => {
        associateAttributes(sku.attributes, sku.id);
      });
      properties.value.forEach((prop) => {
        associateAttributes(prop.attributes, '1');
      });
    }

     // 将 attributes 属性组中的属性在无向图中联系起来
    const associateAttributes = (attributes, skuId) => {
      attributes.forEach((attr1) => {
        attributes.forEach((attr2) => {
          // 因 properties 与 skuList 数据结构不一致，需作处理
          if (attr1 !== attr2 || attr1.value !== attr2.value) {
            if (attr1.value && attr2.value) {
              attr1 = attr1.value;
              attr2 = attr2.value;
            }
            const index1 = vertexList.value.indexOf(attr1);
            const index2 = vertexList.value.indexOf(attr2);
            if (index1 > -1 && index2 > -1) {
              if(matrix.value[index1][index2]) {
                matrix.value[index1][index2].add(skuId);
              }
              else {
                matrix.value[index1][index2] = new Set([skuId]);
              }
            }
          }
        });
      });
    }

     // 判断当前 attribute 是否可选，返回 true 表示可选，返回 false 表示不可选，选项置灰
    const canAttributeSelect = (attribute) => {
        const len = selected.value.length
      if (!selected.value || !len || attribute.isActive) {
        return true;
      }
      let res = [];
      selected.value.forEach((item) => {
        const index1 = vertexList.value.indexOf(item.value);
        const index2 = vertexList.value.indexOf(attribute.value);
        res.push(matrix.value[index1][index2]);
      });

      if(res.some((item)=> (item === 0))) {
        return false;
      }
      else if(res.some((item) => (item.has('1')))) {
        return true;
      }
      else {
        const first = res[0];
        const others = res.slice(1);
        return Array.from(first).some((skuId) => (others.every((item) => (item.has(skuId)))));
      }
    }

    const initPrice = ref('')
    const init = () => {
        properties.value = [ ...propertiesList.value ];
        skuList.value = [ ...skuData.value ];

        const a = properties.value[1].attributes
        const priceList = a.map(item => item.price)

        initPrice.value = Math.min(...priceList) + ' - ' + Math.max(...priceList)
        initEmptyAdjMatrix();
        setAdjMatrixValue();
    }

    const countd = ref(1)
    const confirmSelected = () => {
      if(!Array.isArray(selected.value) || selected.value.length !== 2) {
        uni.showToast({
          title: '请选择商品属性',
          icon: 'none',
          duration: 3000
        })
        return
      }

      infoShow.value = false
      
    }

    const addCar = async () => {
      if(!Array.isArray(selected.value) || selected.value.length !== 2) {
        uni.showToast({
          title: '请选择商品属性',
          icon: 'none',
          duration: 3000
        })
        return
      }

      const size = selected.value[0].value
      const id = selected.value[1].productSkuId
      const number = countd.value
      const objParams = {
        productSkuId: id,
        size,
        number
      }
      const res = await addCarApi(objParams)
      if(res.data) {
        uni.showToast({
          title: '加入购物车成功',
          icon: 'success',
          duration: 3000
        })
      } else {
        uni.showToast({
          title: res.message,
          icon: 'error',
          duration: 3000
        })
      }

    }

    const onBuy = async () => {
      if(!Array.isArray(selected.value) || selected.value.length !== 2) {
        uni.showToast({
          title: '请选择商品属性',
          icon: 'none',
          duration: 3000
        })
        return
      }

      uni.navigateTo({
        url: '/pagesA/pages/address/receiving'
      })      
    }

    const productId = goodsStore.productId
    const JumpComment = () => {
        uni.navigateTo({
            url: `/pagesA/pages/comment/index?productId=${productId}`
        })
    }

    onMounted(() => {
      initInfo()

      init()

      proCommentStore.getCommentListById(goodsStore.productId)
    }) 

    onLoad(() => {
      setPropertiesList()
    })

    // 直接购买
    const payOrder = async (params) => {
      const arr = []
      arr.push(params)
      const res = await createOrderApi(arr)
      const { code, data } = res
      if(code === 200) {
        const response = await orderPayApi(data[0].id)
        if(response.data) {
          uni.showToast({
            title: '购买成功',
            icon: 'success',
            duration: 3000
          })
        }
      }

    }
    onShow(() => {
      if(!Object.keys(addressStore.curAddress).length) return
      if(!Array.isArray(selected.value) || selected.value.length !== 2) {
        uni.showToast({
          title: '请选择商品属性',
          icon: 'none',
          duration: 3000
        })
        return
      }

      const skuId = selected.value[1].productSkuId
      const number = countd.value
      const totalPrice = selected.value[1].price * number
      const size = selected.value[0].value
      const deliveryAddress = JSON.stringify(addressStore.curAddress)
      const params = {
        skuId,
        number,
        totalPrice,
        size,
        deliveryAddress
      }

      payOrder(params)
    })

    const initInfo = () => {
      swiperList.value = [ ...productInfo.value.carousel ]
    }


</script>

<template>
    <view class="bg-#C1C1C1">
        <view class="py-3">
            <u-swiper
                :list="swiperList"
                indicator
                circular
                indicatorActiveColor="#E4697B"
                indicatorMode="dot"
                :displayMultipleItems="0"
            ></u-swiper> 
        </view>
        <view class="bg-#fff p-4">
            <view class="flex flex-col">
                <text class="color-#DC143C font-600">¥ {{ productInfo.priceRange }}</text>
                <text class="mt-3">{{ productInfo.productName }}</text>
            </view>
            <u-divider
                lineColor="#ccc"
            ></u-divider>
            <view class="color-#999">
                <view class="mb-3 layout-slide" @click="chooisGoods">
                    <text class="mr-4 color-#000">选择</text>
                    <view class="flex-1">
                        <text>颜色: </text>
                        <text>尺码</text>
                    </view>
                    <u-icon name="arrow-right" color="#2979ff" size="18"></u-icon>
                </view>
                <view class="mb-3">
                    <text class="mr-4 color-#000">发货</text>
                    <text>快递: </text>
                    <text>从{{ productInfo.deliveryAddress}}发货 , 包邮</text>
                </view>
                <view>
                    <text class="color-#000">服务</text>
                    <text class="mx-4">正品保证</text>
                    <text>七天无理由</text>
                </view>
            </view>
        </view>
        <view class="bg-#fff p-4 my-3">
            <u-divider textSize="18" text="商品描述" dashed :hairline="false"></u-divider>
            <view class="mt-4 flex flex-col items-center">
              <template v-if="!productInfo.descUrls.length">
                <u-empty
                  mode="data"
                >
                </u-empty>
              </template>
              <template v-else>
                <template v-for="(item, index) of productInfo.descUrls" :key="index">
                  <image
                      :src="item"
                      mode="widthFix"
                      class="vertical-bottom"
                  />
                </template>
              </template>
            </view>
        </view>
        <view class="bg-#fff p-4 ">
            <view class="layout-slide" @click="JumpComment">
                <text class="mr-4 color-#000 font-600">商品评论</text>
                <u-icon name="arrow-right" color="#2979ff" size="18"></u-icon>
            </view>

            <view class="mt-3 pb-3">
              <template v-if="!Object.keys(oneComment).length">
                <u-empty
                    mode="data"
                    text="暂无评论"
                >
                </u-empty>
              </template>
              <template v-else>
                <view class="flex gap-5 max-h-42 overflow-hidden">
                    <up-avatar 
                        :src="oneComment.user.avatar"
                        shape="circle"
                    ></up-avatar>
                    <view class="flex flex-col items-start">
                        <view class="w-70 flex justify-between">
                            <text class="text-3 color-#999">{{ oneComment.user.nickname }}</text>
                            <text class="text-3 color-#999">{{ oneComment.createTime }}</text>
                        </view>
                        <view class="my-1">
                            <u-rate
                                :modelValue="oneComment.starRating"
                            ></u-rate>
                        </view>
                          
                        <view class="ellipsis">
                          <text>{{ oneComment.commentContent }}</text>
                          <!-- <template v-if="oneComment.commentImgUrl">
                            <template v-for="url of oneComment.commentImgUrl" :key="url">
                              <image
                                  class="w-25 h-20"
                                  :src="url"
                                  mode="aspectFit"
                              />
                            </template>
                          </template> -->
                        </view>
                    </view>
                </view>
              </template>
            </view>
              
        </view>

        <view class="w-100vw bg-#fff flex mb-5">
          <text 
            class="flex-1 flex justify-center bg-#82A4D7 py-5 rd_l color-#fff font-600"
            @click="addCar"
          >
            加入购物车
          </text>
          <text 
            class="flex-1 flex justify-center bg-#5C90DF py-5 rd_r color-#fff font-600"
            @click="onBuy"
          >
            立即购买
          </text>
        </view>
          
        <u-popup 
            :show="infoShow"
            closeable
            round="10"
            @close="close" 
            @open="open"
        >

            <view class="root">
              <template v-if="selected.length">
                  <view class="thumb-box">
                      <image class="item-menu-image" :src="selected[1]?.img" mode="aspectFit"></image>
                      <view class="ml-3 h-25 flex flex-col justify-around font-600">
                        <text class="color-#FF0000 text-4.5" v-if="selected[1] && selected[1].price">¥ {{ selected[1].price }}</text>
                        <view class="color-#999">
                            <text>已选: </text>
                            <text>{{ selected[0]?.value }}, </text>
                            <text>{{ selected[1]?.value }}</text>
                        </view>
                        <!-- <text 
                          class="color-#999" 
                          v-if="selected[1]?.stock"
                        >库存: {{ selected[1]?.stock }}</text> -->
                    </view>
                  </view>
                </template>
              <template v-else>
                <view class="thumb-box">
                      <image 
                        class="item-menu-image" 
                        :src="swiperList[0]" 
                        mode="aspectFit"
                      ></image>
                      <view class="ml-3 h-25 flex flex-col justify-around font-600">
                        <text class="color-#FF0000 text-4.5">¥ {{ initPrice }}</text>
                        <view class="color-#999">
                          <text>请选择: </text>
                          <text>尺码</text>
                          <text>样式</text>
                        </view>
                    </view>
                </view>
              </template>
              <view v-for="(property, propertyIndex) in properties" :key="propertyIndex">
                  <p class="mb-3">{{ property.name }}</p>
                  <view class="sku-box-area">
                      <template v-for="(attribute, attributeIndex) in property.attributes" :key="attributeIndex">
                          <view
                              :class="[
                                { 'style-active': property.name == '样式' },
                                'sku-box',
                                'sku-text',
                                attribute.isActive ? 'active' : '',
                                attribute.isDisabled ? 'disabled' : '',
                              ]"
                              @click="handleClickAttribute(propertyIndex, attributeIndex)"
                          >
                              <view class="flex flex-col items-center">
                                <image
                                  :src="attribute.img"
                                  mode="aspectFit"
                                  class="attribute_img"
                                  v-if="property.name === '样式'"
                                />
                                <text>{{ attribute.value }}</text>
                                <text class="color-#FF0000" v-if="attribute.price">¥ {{ attribute.price }}</text>
                              </view>
                          </view>
                      </template>
                  </view>
              </view>
              <view class="flex justify-between my-3">
                <text>购买数量</text>
                <u-number-box integer v-model="countd"></u-number-box>
              </view>
              <view 
                class="layout-center bg-#7DA1DC color-#fff py-3 rd-2"
                @click="confirmSelected"
              >确认</view>
            </view>
        </u-popup>
    </view>
</template>

<style scoped>
   .root {
      padding: 18px;
    }

    .sku-box-area {
    display: flex;
    flex: 1;
    flex-direction: row;
    flex-wrap: wrap;
    }
    .sku-box {
    border: 1px solid #cccccc;
    border-radius: 6px;
    margin-right: 12px;
    padding: 8px 20px;
    margin-bottom: 10px;
    }

    .style-active {
      padding-left: 0;
      padding-right: 0;
    }

    .sku-text {
    font-size: 16px;
    line-height: 16px;
    color: #666666;
    }
    .active {
    border-color: #ff6600;
    color: #ff6600;
    }
    .disabled {
    opacity: 0.5;
    border-color: #e0e0e0;
    color: #999999;
    }

    .attribute_img {
      width: 98px; 
      height: 80px;
    }

    .thumb-box {
      width: 100%;
      display: flex;
      align-items: center;
      margin-top: 20rpx;
      padding-bottom: 20rpx;
      border-bottom: 1px solid #D8DEEB;
    }

  .item-menu-image {
		width: 200rpx;
		height: 200rpx;
	}

  .rd_l {
    border-top-left-radius: 8px;
    border-bottom-left-radius: 8px;
  }

  .rd_r {
    border-top-right-radius: 8px;
    border-bottom-right-radius: 8px;
  }
</style>
