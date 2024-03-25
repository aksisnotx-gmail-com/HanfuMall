package com.app.domain.order.service;

import com.app.domain.base.AbstractService;
import com.app.domain.order.enmus.OrderState;
import com.app.domain.order.entity.OrderDetailsEntity;
import com.app.domain.order.entity.OrderEntity;
import com.app.domain.order.mapper.OrderMapper;
import com.app.domain.order.param.OrderParam;
import com.app.domain.product.entity.ProductSkuEntity;
import com.app.domain.product.service.ProductDetailsService;
import com.app.domain.product.service.ProductSkuService;
import com.app.domain.user.entity.UserEntity;
import com.app.toolkit.web.CommonPageRequestUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sdk.util.asserts.AssertUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static com.app.domain.order.service.OrderService.CACHE_KEY;

/**
 * @author xxl
 * @since 2024/3/21
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = CACHE_KEY)
public class OrderService extends AbstractService<OrderMapper, OrderEntity> {

    private final ProductSkuService skuService;

    private final ProductDetailsService productDetailsService;

    private final OrderAction orderAction;

    private  final OrderDetailsService detailsService;

    /**
     * 兼容查询返回是List/Page
     */
    private final static long MAX_SIZE = 9999;

    public static final String CACHE_KEY = "ORDER_SERVICE";

    /**
     * 查看用户是否买了这个商品中的某一款商品
     *
     * @param userId 用户ID
     * @param productId 商品Id
     */
    public boolean hasProduct(String userId, String productId) {
      //查询买过的订单
      return   queryCompleteOrderByUserId(userId,MAX_SIZE).
                getRecords().
                stream().
                map(OrderEntity::getId).
                anyMatch(t ->
                        //查询买过订单包含的商品
                    queryOrderDetailsByOrderId(t,MAX_SIZE).
                            getRecords().
                            parallelStream().
                            anyMatch(t1 -> productId.equals(t1.getProductDetail().getId()))
                );
    }

    /**
     * 获取已经完成的订单
     *
     * @param userId 用户ID
     * @return 已经完成的订单
     */
    public Page<OrderEntity> queryCompleteOrderByUserId(String userId,long size) {
        Page<OrderEntity> page = CommonPageRequestUtils.defaultPage();
        //兼容请求调用
        if (size != 0) {
            page.setSize(size);
        }
        return this.lambdaQuery().
                eq(OrderEntity::getUserId, userId).
                eq(OrderEntity::getState, OrderState.CONFIRM_RECEIPT.name()).
                page(page);
    }

    public Page<OrderDetailsEntity> queryOrderDetailsByOrderId(String orderId,long size) {
        Page<OrderDetailsEntity> page = CommonPageRequestUtils.defaultPage();
        //兼容请求调用
        if (size != 0) {
            page.setSize(size);
        }
        return detailsService.lambdaQuery().eq(OrderDetailsEntity::getOrderId,orderId).page(page);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public  Boolean createOrder(OrderParam orderParam,String userId) {
        OrderEntity order = OrderEntity.create(OrderState.PLACE_ORDER, userId, orderParam.getDeliveryAddress());
        boolean save = this.save(order);
        //保存商品详情
        List<OrderDetailsEntity> list = orderParam.getProductSkuIds().stream().map(t -> {
            ProductSkuEntity entity = skuService.getById(t);
            //如果库存满足条件则扣减
            skuService.checkStock(entity.getStock(), t.getNumber());
            skuService.reduceSkuStock(entity.getProductId(), t.getNumber());
            return OrderDetailsEntity.create(entity, productDetailsService.getById(entity.getProductId()), order.getId(), t.getNumber());
        }).toList();
        return save && detailsService.saveBatch(list);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean closeOrder(String orderId, UserEntity user) {
        OrderState closeOrder = OrderState.CLOSE_ORDER;
        OrderEntity one = getOne(orderId, closeOrder, user);
        //如果允许关闭订单 , 1.订单中的商品如果存在数量加回来 2.订单中的商品不存在则忽略
        List<OrderDetailsEntity> details = getDetailByOrderId(one.getId());
        details.forEach(t ->{
            ProductSkuEntity sku = skuService.getById(t.getProductSku().getProductId(), false);
            if (!Objects.isNull(sku)) {
                //存在
                skuService.addSkuStock(sku.getId(),t.getSkuNumber());
            }
        });
        one.setState(closeOrder);
        return this.updateById(one);
    }

    public Boolean payOrder(String orderId, UserEntity user) {
        OrderEntity one = getOne(orderId, OrderState.MAKE_PAYMENT, user);

        return
    }

    private  OrderEntity getOne(String orderId,OrderState nextState,UserEntity user) {
        OrderEntity entity = this.lambdaQuery().eq(OrderEntity::getId, orderId).eq(OrderEntity::getUserId, user.getId()).one();
        AssertUtils.notNull(entity, "订单不存在");
        orderAction.doAction(entity.getState(), nextState, user.getRole());
        return entity;
    }

    private List<OrderDetailsEntity> getDetailByOrderId(String orderId) {
        return detailsService.lambdaQuery().eq(OrderDetailsEntity::getOrderId, orderId).list();
    }

}
