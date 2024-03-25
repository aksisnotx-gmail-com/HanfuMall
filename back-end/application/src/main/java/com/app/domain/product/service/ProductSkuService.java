package com.app.domain.product.service;

import com.app.domain.base.AbstractService;
import com.app.domain.product.entity.ProductSkuEntity;
import com.app.domain.product.mapper.ProductSkuMapper;
import com.sdk.util.asserts.AssertUtils;
import org.springframework.stereotype.Service;

/**
 * @author xxl
 * @since 2024/3/19
 */
@Service
public class ProductSkuService extends AbstractService<ProductSkuMapper, ProductSkuEntity> {

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
            entity.setStock(stock + (-num));
            this.updateById(entity);
        }
    }

    /**
     * 增加库存
     *
     * @param skuId  skuId
     * @param num 减少的数量
     */
    public void addSkuStock(String skuId, Integer num) {
        ProductSkuEntity entity = this.getById(skuId);
        Integer stock = entity.getStock();
        //更新库存
        entity.setStock(stock + num);
        this.updateById(entity);
    }

    public void checkStock(Integer stock, Integer num) {
        AssertUtils.assertTrue(num > 0, "数量必须要大于0");
        //如果库存数量 < 要减的数量  表示库存不足
        AssertUtils.assertTrue( stock > num, "库存不足");
    }
}
