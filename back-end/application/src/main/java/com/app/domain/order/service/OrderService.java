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
import com.app.domain.user.enums.Role;
import com.app.domain.wallet.entity.WalletEntity;
import com.app.domain.wallet.service.WalletService;
import com.app.toolkit.web.CommonPageRequestUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sdk.util.asserts.AssertUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * @author xxl
 * @since 2024/3/21
 */
@Service
@RequiredArgsConstructor
public class OrderService extends AbstractService<OrderMapper, OrderEntity> {

    private final ProductSkuService skuService;

    private final ProductDetailsService productDetailsService;

    private final OrderAction orderAction;

    private final OrderDetailsService detailsService;

    private final WalletService walletService;

    /**
     * 查看用户是否买了这个商品中的某一款商品
     *
     * @param userId 用户ID
     * @param productId 商品Id
     */
    public boolean hasProduct(String userId, String productId) {
        //查询买过(确认收货)的订单
        return  this.lambdaQuery().
                eq(OrderEntity::getUserId,userId).
                eq(OrderEntity::getState,OrderState.CONFIRM_RECEIPT.name()).list().
                stream().
                map(OrderEntity::getId).
                anyMatch(t ->
                        //查询买过订单包含的商品
                        detailsService.getDetailsByOrderId(t).
                                parallelStream().
                                anyMatch(t1 -> productId.equals(t1.getProductDetail().getId()))
                );
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public  Boolean createOrder(OrderParam orderParam,String userId) {
        //未保存发货地相同现在需要把订单分类
        //多少个商品
        List<ProductSkuEntity> skus = orderParam.getProductSkuIds().stream().map(t -> skuService.getById(t.getSkuId())).toList();

        //要创建多少表单
        List<String> productIds = skus.parallelStream().map(ProductSkuEntity::getProductId).distinct().toList();
        for (String productId : productIds) {
            OrderEntity order = OrderEntity.create(OrderState.PLACE_ORDER, userId, orderParam.getDeliveryAddress());
            //保存订单
            this.save(order);
            //保存商品详情
            List<OrderDetailsEntity> list = skus.
                    stream().
                    //属于此类商品的才能是一个单子
                    filter(t -> productId.equals(t.getProductId())).
                    map(t -> {
                        //筛选出此商品要购买的数量
                        OrderParam.OrderDetailsParam param = orderParam.getProductSkuIds().parallelStream().filter(t1 -> t1.getSkuId().
                                        equals(t.getId())).
                                findFirst().
                                orElse(null);
                        AssertUtils.notNull(param, "订单异常请重新下单");

                        //如果库存满足条件则扣减,也就是说后面没必要检查库存
                        skuService.checkStock(t.getStock(), param.getNumber());
                        //设置购买数量
                        skuService.reduceSkuStock(t.getId(), param.getNumber());
                        return OrderDetailsEntity.create(t,
                                productDetailsService.getById(t.getProductId()),
                                order.getId(), param.getNumber(), param.getTotalPrice());
            }).toList();

            //保存商品
            detailsService.saveBatch(list);
        }

        return true;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean closeOrder(String orderId, UserEntity user) {
        OrderState closeOrder = OrderState.CLOSE_ORDER;
        OrderEntity one = getOne(orderId, closeOrder, user);
        //如果允许关闭订单 , 1.订单中的商品如果存在数量加回来 2.订单中的商品不存在则忽略
        List<OrderDetailsEntity> details = detailsService.getDetailsByOrderId(one.getId());
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

    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean payOrder(String orderId, UserEntity user) {
        OrderState makePayment = OrderState.MAKE_PAYMENT;
        OrderEntity one = getOne(orderId,makePayment , user);
        WalletEntity wallet = walletService.getOne(user.getId());
        //获取账单总余额
        BigDecimal price = detailsService.getProductTotalPrice(one.getId());
        //获取余额
        AssertUtils.assertTrue(wallet.getBalance().compareTo(price) >= 0, "余额不足,请前往汉币中心充值");
        //余额 - 商品总价钱
        wallet.setBalance(wallet.getBalance().subtract(price));
        //设置状态
        one.setState(makePayment);
        return this.updateById(one) && walletService.updateById(wallet);
    }

    public Boolean applyRefundOrder(String orderId, UserEntity loginUser) {
        OrderState refund = OrderState.APPLY_FOR_REFUND;
        OrderEntity one = getOne(orderId, refund,loginUser);
        one.setState(refund);
        return this.updateById(one);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean refundOrder(String orderId,UserEntity user) {
        OrderState refund = OrderState.REFUND;
        OrderEntity one = getOne(orderId, refund,user);
        one.setState(refund);
        //获取账单总余额
        BigDecimal price = detailsService.getProductTotalPrice(one.getId());
        WalletEntity wallet = walletService.getOne(user.getId());
        //余额 + 商品总价钱
        wallet.setBalance(wallet.getBalance().add(price));
        //更新订单 && 钱包
        return this.updateById(one) && walletService.updateById(wallet);
    }

    public Boolean sendOrder(String orderId, UserEntity loginUser) {
        OrderState send = OrderState.SHIP_ORDER;
        OrderEntity one = getOne(orderId, send, loginUser);
        one.setState(send);
        return this.updateById(one);
    }

    public Boolean receiveOrder(String orderId, UserEntity loginUser) {
        OrderState receive = OrderState.CONFIRM_RECEIPT;
        OrderEntity one = getOne(orderId, receive, loginUser);
        one.setState(receive);
        //更新未未评价
        List<OrderDetailsEntity> list = detailsService.getDetailsByOrderId(one.getId()).parallelStream().peek(t -> t.setIsEvaluate(OrderDetailsEntity.UN_EVALUATE)).toList();
        return this.updateById(one) && detailsService.updateBatchById(list);
    }

    public Boolean deleteOrder(String orderId, UserEntity loginUser) {
        OrderState delete = OrderState.DELETE_ORDER;
        OrderEntity one = getOne(orderId, delete, loginUser);
        return this.removeById(one.getId());
    }

    public Page<OrderEntity> getOrderByUser(UserEntity loginUser) {
        //用户获取自己的，管理员获取所有的
        Page<OrderEntity> page;
        if (Role.ADMIN.equals(loginUser.getRole())) {
            page = this.lambdaQuery().page(CommonPageRequestUtils.defaultPage());
        }else {
            page = this.lambdaQuery().eq(OrderEntity::getUserId, loginUser.getId()).page(CommonPageRequestUtils.defaultPage());
        }

        //查询订单详情
        page.getRecords().forEach(t -> t.setOrderDetails(detailsService.getDetailsByOrderId(t.getId())));
        return page;
    }

    public OrderEntity getOrder(String orderId) {
        OrderEntity entity = this.lambdaQuery().eq(OrderEntity::getId, orderId).one();
        entity.setOrderDetails(detailsService.getDetailsByOrderId(entity.getId()));
        return entity;
    }


    public Page<OrderEntity> getWaitPay(String loginUserId) {
        return getOrderByUserId(loginUserId,OrderEntity::getState,OrderState.PLACE_ORDER.name());
    }

    public Page<OrderEntity> getWaitReceive(String loginUserId) {
        return getOrderByUserId(loginUserId,OrderEntity::getState,OrderState.SHIP_ORDER.name());
    }

    public Page<OrderEntity> getWaitEvaluate(String loginUserId) {
        //查询出当前用户已经收货的商品
        Page<OrderEntity> page = getOrderByUserId(loginUserId, OrderEntity::getState, OrderState.CONFIRM_RECEIPT.name());
        List<OrderEntity> list = page.getRecords().
                stream().
                //找出订单详情中未评价的商品
                        peek(t -> t.setOrderDetails(detailsService.getUnEvaluateByOrderId(t.getId()))).
                //把没有未评价的商品筛选出去
                filter(t -> !t.getOrderDetails().isEmpty()).toList();
        page.setTotal(list.size());
        page.setRecords(list);
        return page;
    }
    

    private OrderEntity getOne(String orderId,OrderState nextState,UserEntity user) {
        OrderEntity entity = this.lambdaQuery().eq(OrderEntity::getId, orderId).eq(OrderEntity::getUserId, user.getId()).one();
        AssertUtils.notNull(entity, "订单不存在");
        orderAction.doAction(entity.getState(), nextState, user.getRole());
        return entity;
    }

    private Page<OrderEntity> getOrderByUserId(String userId, SFunction<OrderEntity,?> sFunction, Object val) {
        Page<OrderEntity> page = this.lambdaQuery().eq(OrderEntity::getUserId, userId).eq(sFunction, val).page(CommonPageRequestUtils.defaultPage());
        //查询订单详情
        page.getRecords().forEach(t -> t.setOrderDetails(detailsService.getDetailsByOrderId(t.getId())));
        return page;
    }
}
