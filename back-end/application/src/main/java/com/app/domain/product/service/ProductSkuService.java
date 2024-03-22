package com.app.domain.product.service;

import com.app.domain.base.AbstractService;
import com.app.domain.product.entity.ProductDetailsEntity;
import com.app.domain.product.entity.ProductSkuEntity;
import com.app.domain.product.mapper.ProductDetailsMapper;
import com.app.domain.product.mapper.ProductSkuMapper;
import com.sdk.util.asserts.AssertUtils;
import org.springframework.stereotype.Service;

/**
 * @author xxl
 * @since 2024/3/19
 */
@Service
public class ProductSkuService extends AbstractService<ProductSkuMapper, ProductSkuEntity> {

    //todo 对于处在订单中的商品需要被冻结

    /**
     * 减少库存
     *
     * @param skuId  skuId
     * @param num 减少的数量
     */
    public void reduceSkuStock(String skuId, Integer num) {
        ProductSkuEntity entity = this.getById(skuId);
        Integer stock = entity.getStock();
        synchronized (this) {
            checkStock(stock,num);
            //更新库存
            entity.setStock(stock + num);
            this.updateById(entity);
        }
    }

    private void checkStock(Integer stock, Integer num) {
        int theActualAmount = num + stock;
        //如果库存数量 > 需要增加的数量/相减的数量 + 库存 表示库存不足
        AssertUtils.assertTrue(stock > theActualAmount, "库存不足");
        //0 < 需要增加的数量/相减的数量 + 库存  表示库存不足
        AssertUtils.assertTrue(0 < theActualAmount, "库存不足");
    }

}
