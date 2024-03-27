import { defineStore } from 'pinia'
import { getProductByIdApi } from '@/api/home'
export const useGoodsStore = defineStore('goods', {
    state: () => ({
        productId: '', // 标题ID
        productInfo: {
          carousel: [], // 轮播图
          priceRange: 0, // 价格区间
          productName: '', // 商品名称
          deliveryAddress: '', // 发货地址
          descUrls: [], // 详情图
        }, // 商品信息
        propertiesList: [
            {
              id: "1",
              name: "尺码",
              attributes: [],
            },
            {
              id: "2",
              name: "样式",
              attributes: [],
            }
        ],
        skuData: [],
        goodsItem: {}
    }),
    actions: {
        async setPropertiesList () {
          const id = this.productId
          if(!id) return

          const res = await getProductByIdApi(id)
          const { 
            carousel,
            productName,
            deliveryAddress,
            descUrls,
            specCombinationList,
            specList
          } = res.data
          this.productInfo.carousel = carousel
          this.productInfo.productName = productName
          this.productInfo.deliveryAddress = deliveryAddress
          this.productInfo.descUrls = descUrls
          this.productInfo.priceRange = calculatePriceRange(specCombinationList)

          this.propertiesList[0].attributes = getUniqueFlatArray(specList.size)
          this.propertiesList[1].attributes = getStyleList(specCombinationList)

          this.skuData = transformSpecCombinationToList(specCombinationList)

        }
    }
})

/**
 * @description 计算价格区间
 * @param {Array} specCombinationList 
 */
function calculatePriceRange(specCombinationList) {
  if (!Array.isArray(specCombinationList) || !specCombinationList.length) return

  const prices = specCombinationList.map(item => item.price)

  if (prices.length === 1) {
    // 如果数组中所有价格相同，则只返回这一个价格
    return prices[0];
  } else {
    // 返回价格区间，即最低价格和最高价格
    const minPrice = Math.min(...prices);
    const maxPrice = Math.max(...prices);
    
    return maxPrice == minPrice ? minPrice : `${minPrice} - ${maxPrice}`;
  }
}

/**
 * @description 获取数组中不重复的元素
 * @param {Array[Array]} twoDimensionalArray 
 * @returns {Array}
 */
function getUniqueFlatArray(twoDimensionalArray) {
  // 将二维数组平铺为一维数组
  const flatArray = twoDimensionalArray.flat();
  // 使用Set对象去重，再将Set转换回数组
  const uniqueArray = Array.from(new Set(flatArray));

  if(!uniqueArray.length) return

  return uniqueArray.map(item => ({ value: item, isActive: false, isDisabled: false }));
}

/**
 * @description 获取商品信息
 * @param {Array} productList 
 * @returns 
 */
function getStyleList (productList) {
  if(!productList.length) return

  return productList.map(item => 
    ({ 
      value: item.desc, 
      img: item.carouselUrl,
      price: item.price,
      stock: item.stock,
      productSkuId: item.id,
      isActive: false, 
      isDisabled: false 
    }))
}

/**
 * @description 将规格组合转换为skuData格式
 * @param {Array} specCombinationList 
 * @returns 
 */
function transformSpecCombinationToList(specCombinationList) {
  if(!Array.isArray(specCombinationList) || !specCombinationList.length) return
  const skuData = [];

  specCombinationList.forEach(item => {
    item.size.forEach(size => {
      skuData.push({
        id: item.id,
        productId: item.productId,
        attributes: [size, item.desc]
      });
    });
  });

  return skuData;
}