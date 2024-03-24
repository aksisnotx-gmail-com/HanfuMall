<script setup>
    import { useGoodsStore } from '@/store/modules/goods.js'
    import { propertiesList, skuData } from './data';
    const goodsStore = useGoodsStore()

    const swiperList = ref([
        'https://cdn.uviewui.com/uview/swiper/swiper3.png',
        'https://cdn.uviewui.com/uview/swiper/swiper2.png',
        'https://cdn.uviewui.com/uview/swiper/swiper1.png',
    ])

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
        properties.value = [ ...propertiesList ];
        skuList.value = [ ...skuData ];

        const a = propertiesList[1].attributes
        const priceList = a.map(item => item.price)

        initPrice.value = Math.min(...priceList) + ' - ' + Math.max(...priceList)
        initEmptyAdjMatrix();
        setAdjMatrixValue();
    }

    const rate = ref(4)

    const JumpComment = () => {
        // TODO 商品id 跳转到该商品的评价页面
        uni.navigateTo({
            url: '/pagesA/pages/comment/index'
        })
    }

    const countd = ref(1)

    onMounted(() => {
        init()
    }) 

    onLoad(() => {
      
    })
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
            ></u-swiper> 
        </view>
        <view class="bg-#fff p-4">
            <view class="flex flex-col">
                <text class="color-#DC143C font-600">¥ 69.00 - 161.00</text>
                <text class="mt-3">绾青丝【凤凰鸣】原创明制仿妆花马面裙套装汉服女2023新款秋冬</text>
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
                    <text>包邮</text>
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
                <image
                    src="/pagesA/static/ms1.jpg"
                    mode="widthFix"
                    class="vertical-bottom"
                />
                <image
                    src="/pagesA/static/ms2.jpg"
                    mode="widthFix"
                    class="vertical-bottom"
                />
                <image
                    src="/pagesA/static/ms3.jpg"
                    mode="widthFix"
                    class="vertical-bottom"
                />
                <image
                    src="/pagesA/static/ms.jpg"
                    mode="widthFix"
                    class="vertical-bottom"
                />
            </view>
        </view>
        <view class="bg-#fff p-4 ">
            <view class="layout-slide" @click="JumpComment">
                <text class="mr-4 color-#000 font-600">商品评论</text>
                <u-icon name="arrow-right" color="#2979ff" size="18"></u-icon>
            </view>
            <view class="mt-3 pb-3">
                <view class="flex gap-5 max-h-42 overflow-hidden">
                    <up-avatar 
                        src="https://ts2.cn.mm.bing.net/th?id=OIP-C.54qlbLNAZ64K94c_DCT-qAAAAA&w=166&h=166&c=8&rs=1&qlt=90&o=6&pid=3.1&rm=2" 
                        shape="circle"
                    ></up-avatar>
                    <view class="flex flex-col items-start">
                        <view class="w-70 flex justify-between">
                            <text class="text-3 color-#999">用户名</text>
                            <text class="text-3 color-#999">2020/1/2</text>
                        </view>
                        <view class="my-1">
                            <u-rate
                                :modelValue="rate"
                            ></u-rate>
                        </view>
                          
                        <view class="ellipsis">
                            《叶圣陶散文》为“名家经典珍藏”丛书之一，收录了叶圣陶先生的散文精品数十篇。这些作品内容丰富，题材各异，构思精巧，文笔精巧、语言幽默、内蕴深厚、风格恬淡，充分显示了叶圣陶先生的文学功底及丰富的人生阅历，从一个侧面反映了作者的思想感情及创作风格，非常值得一读。叶圣陶是20世纪中国一位杰出的作家、教育家和出版家，又是中国现代儿童文学创作的先行者。作为散文家，他早期和周作人、朱自清共同成为文学研究会散文创作的中坚，后来又成为开明派散文的代表，其散文被一九三五年出版的《中国新文学大系》选录的篇数仅次于周作人、鲁迅和朱自清。《叶圣陶散文》为“名家经典珍藏”丛书之一，收录了叶圣陶先生的散文精品数十篇。这些作品内容丰富，题材各异，构思精巧，文笔精巧、语言幽默、内蕴深厚、风格恬淡，充分显示了叶圣陶先生的文学功底及丰富的人生阅历，从一个侧面反映了作者的思想感情及创作风格，非常值得一读。叶圣陶是20世纪中国一位杰出的作家、教育家和出版家，又是中国现代儿童文学创作的先行者。作为散文家，他早期和周作人、朱自清共同成为文学研究会散文创作的中坚，后来又成为开明派散文的代表，其散文被一九三五年出版的《中国新文学大系》选录的篇数仅次于周作人、鲁迅和朱自清。《叶圣陶散文》为“名家经典珍藏”丛书之一，收录了叶圣陶先生的散文精品数十篇。这些作品内容丰富，题材各异，构思精巧，文笔精巧、语言幽默、内蕴深厚、风格恬淡，充分显示了叶圣陶先生的文学功底及丰富的人生阅历，从一个侧面反映了作者的思想感情及创作风格，非常值得一读。叶圣陶是20世纪中国一位杰出的作家、教育家和出版家，又是中国现代儿童文学创作的先行者。作为散文家，他早期和周作人、朱自清共同成为文学研究会散文创作的中坚，后来又成为开明派散文的代表，其散文被一九三五年出版的《中国新文学大系》选录的篇数仅次于周作人、鲁迅和朱自清。
                        </view>
                    </view>
                </view>
            </view>
              
        </view>
          
        <u-popup 
            :show="infoShow"
            closeable
            round="10"
            @close="close" 
            @open="open"
        >

            <view class="root">
              <block v-if="selected.length">
                  <view class="thumb-box">
                      <image class="item-menu-image" :src="selected[1]?.img" mode="aspectFit"></image>
                      <view class="ml-3 h-25 flex flex-col justify-around font-600">
                        <text class="color-#FF0000 text-4.5">¥ {{ selected[1]?.price }}</text>
                        <view class="color-#999">
                          
                            <text>已选: </text>
                            <text>{{ selected[0]?.value }}, </text>
                            <text>{{ selected[1]?.value }}</text>
                        </view>
                    </view>
                  </view>
              </block>
              <block v-else>
                <view class="thumb-box">
                      <image class="item-menu-image" src="https://cdn.uviewui.com/uview/swiper/swiper3.png" mode="aspectFit"></image>
                      <view class="ml-3 h-25 flex flex-col justify-around font-600">
                        <text class="color-#FF0000 text-4.5">¥ {{ initPrice }}</text>
                        <view class="color-#999">
                          <text>请选择: </text>
                          <text>尺码</text>
                          <text>样式</text>
                        </view>
                    </view>
                </view>
              </block>
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
                              </view>
                          </view>
                      </template>
                  </view>
              </view>
              <view class="flex justify-between my-3">
                <text>购买数量</text>
                <u-number-box integer v-model="countd"></u-number-box>
              </view>
              <view class="layout-center bg-#7DA1DC color-#fff py-3 rd-2">确认</view>
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
</style>
