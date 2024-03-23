package com.app.domain.shippingcart.service;

import cn.hutool.json.JSONUtil;
import com.app.domain.base.AbstractService;
import com.app.domain.product.entity.ProductDetailsEntity;
import com.app.domain.product.entity.ProductSkuEntity;
import com.app.domain.product.service.ProductDetailsService;
import com.app.domain.product.service.ProductSkuService;
import com.app.domain.shippingcart.entity.ShoppingCartEntity;
import com.app.domain.shippingcart.mapper.ShoppingCartMapper;
import com.app.toolkit.web.CommonPageRequestUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sdk.exception.GlobalException;
import com.sdk.util.asserts.AssertUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.app.domain.product.service.ProductDetailsService.PRODUCT;
import static com.app.domain.product.service.ProductDetailsService.SKU;

/**
 * @author xxl
 * @since 2024/3/22
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = ShoppingCartService.SHOPPING_CART)
public class ShoppingCartService extends AbstractService<ShoppingCartMapper, ShoppingCartEntity> {

    public static final String SHOPPING_CART = "SHOPPING_CART";

    private final ProductDetailsService productService;

    private final ProductSkuService skuService;

    @CacheEvict(allEntries = true)
    public synchronized Boolean addSku(ShoppingCartEntity entity, String loginUserId) {
        //购物数量
        Integer number = entity.getNumber();
        AssertUtils.assertTrue(number > 0, "购买数量不能小于等于0");
        //库存数量
        Integer stock = skuService.getById(entity.getProductSkuId()).getStock();
        if (number > stock) {
            throw new GlobalException("库存不足");
        }

        //假设同款的SKU多次添加
        List<ShoppingCartEntity> list = this.lambdaQuery().eq(ShoppingCartEntity::getUserId, loginUserId).eq(ShoppingCartEntity::getProductSkuId, entity.getProductSkuId()).list();
        AssertUtils.assertTrue(list.isEmpty(), "同款的SKU已存在 , 接口调用错误");
        entity.setUserId(loginUserId);
        return this.save(entity);
    }

    @CacheEvict(allEntries = true)
    public synchronized Boolean addOrReduce(String itemId, Integer number) {
        ShoppingCartEntity entity = this.getById(itemId);
        int sumNumber = entity.getNumber() + number;

        //总数量为0则购物车没有这个购物项
        if (sumNumber <= 0) {
            return this.removeById(entity.getId());
        }

        //库存
        Integer stock = skuService.getById(entity.getProductSkuId()).getStock();
        if (sumNumber > stock) {
            throw new GlobalException("库存不足");
        }

        //更新
        entity.setNumber(sumNumber);
        return this.updateById(entity);
    }

    //@Cacheable(key = "#loginUserId")
    public Page<ShoppingCartEntity> getAll(String loginUserId) {
        Page<ShoppingCartEntity> page = this.lambdaQuery().eq(ShoppingCartEntity::getUserId, loginUserId).page(CommonPageRequestUtils.defaultPage());
        //err: 这里不适用JSON转则会出问题
        page.getRecords().forEach(t -> t.setProductMap(productService.getProductBySkuId(t.getProductSkuId())));
        return page;
    }
}
