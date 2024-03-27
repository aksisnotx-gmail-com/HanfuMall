package com.app.domain.order.service;

import com.app.domain.base.AbstractService;
import com.app.domain.comment.entity.ProductCommentEntity;
import com.app.domain.order.enmus.OrderState;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.app.domain.order.enmus.OrderState.MAKE_PAYMENT;

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

    private final WalletService walletService;

    /**
     * 查看用户是否买了这个商品中
     *
     * @param userId 用户ID
     */
    public Boolean hasOrder(String userId,String orderId) {
        //查询买过(确认收货)的订单
        return this.lambdaQuery().
                //用户ID
                eq(OrderEntity::getUserId, userId).
                //订单ID
                eq(OrderEntity::getId, orderId).
                //订单状态
                eq(OrderEntity::getState, OrderState.CONFIRM_RECEIPT.name()).exists();
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public  List<OrderEntity> createOrder(List<OrderParam> orderParam,String userId) {
        List<OrderEntity> list = orderParam.stream().map(t -> {
            ProductSkuEntity entity = skuService.getById(t.getSkuId());
            //如果库存满足条件则扣减,也就是说后面没必要检查库存
            skuService.checkStock(entity.getStock(), t.getNumber());
            //设置购买数量
            skuService.reduceSkuStock(entity.getId(), t.getNumber());
            //创建订单
            return OrderEntity.create(OrderState.PLACE_ORDER,
                    userId,
                    t.getDeliveryAddress(),
                    entity,
                    productDetailsService.getById(entity.getProductId()),
                    t.getNumber(),
                    t.getTotalPrice(),
                    t.getSize());
        }).toList();
        return this.saveBatch(list) ? list : new ArrayList<>();
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean closeOrder(String orderId, UserEntity user) {
        OrderState closeOrder = OrderState.CLOSE_ORDER;
        OrderEntity one = getOne(orderId, closeOrder, user);
        //如果允许关闭订单 , 1.订单中的商品如果存在数量加回来 2.订单中的商品不存在则忽略
        ProductSkuEntity sku = skuService.getById(one.getProductSku().getId(), false);
        if (!Objects.isNull(sku)) {
            //存在
            skuService.addSkuStock(sku.getId(),one.getSkuNumber());
        }
        one.setState(closeOrder);
        return this.updateById(one);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean payOrder(String orderId, UserEntity user) {
        OrderState makePayment = MAKE_PAYMENT;
        OrderEntity one = getOne(orderId,makePayment , user);
        WalletEntity wallet = walletService.getOne(user.getId());
        //获取账单总余额
        BigDecimal price = one.getTotalPrice();
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
        //管理员做的事不需要买家
        OrderEntity one = getOneByAdmin(orderId, refund,user);
        one.setState(refund);
        ProductSkuEntity entity = skuService.getById(one.getProductSku().getId(), false);
        //如果商品存在则返回库存否则不返回
        if (!Objects.isNull(entity)) {
            entity.setStock(entity.getStock() + one.getSkuNumber());
            skuService.updateById(entity);
        }
        //获取账单总余额
        BigDecimal price = one.getTotalPrice();
        //获取订单人的钱包
        WalletEntity wallet = walletService.getOne(one.getUserId());
        //余额 + 商品总价钱
        wallet.setBalance(wallet.getBalance().add(price));
        //更新订单 && 钱包 && 更新商品
        return this.updateById(one) && walletService.updateById(wallet);
    }

    public Boolean sendOrder(String orderId, UserEntity loginUser) {
        OrderState send = OrderState.SHIP_ORDER;
        //不需要获取管理员用户
        OrderEntity one = getOneByAdmin(orderId, send, loginUser);
        one.setState(send);
        return this.updateById(one);
    }

    public Boolean receiveOrder(String orderId, UserEntity loginUser) {
        OrderState receive = OrderState.CONFIRM_RECEIPT;
        OrderEntity one = getOne(orderId, receive, loginUser);
        one.setState(receive);
        //更新未未评价
        one.setIsEvaluate(OrderEntity.UN_EVALUATE);
        return this.updateById(one);
    }


    public Boolean deleteOrder(String orderId, UserEntity loginUser) {
        OrderState delete = OrderState.DELETE_ORDER;
        OrderEntity one = getOne(orderId, delete, loginUser);
        return this.removeById(one.getId());
    }

    public Page<OrderEntity> getOrderByUser(UserEntity loginUser) {
        //用户获取自己的,管理员获取所有的
        Page<OrderEntity> page;
        if (Role.ADMIN.equals(loginUser.getRole())) {
            page = this.lambdaQuery().page(CommonPageRequestUtils.defaultPage());
        }else {
            page = this.lambdaQuery().eq(OrderEntity::getUserId, loginUser.getId()).page(CommonPageRequestUtils.defaultPage());
        }
        return page;
    }

    public OrderEntity getOrder(String orderId) {
        return this.lambdaQuery().eq(OrderEntity::getId, orderId).one();
    }


    public Page<OrderEntity> getWaitPay(UserEntity user) {
        return getOrderByUserId(user,OrderEntity::getState,OrderState.PLACE_ORDER.name());
    }

    public Page<OrderEntity> getWaitReceive(UserEntity user) {
        return getOrderByUserId(user,OrderEntity::getState,OrderState.SHIP_ORDER.name(),OrderState.MAKE_PAYMENT.name());
    }

    public Page<OrderEntity> getWaitEvaluate(UserEntity user) {
        //查询出当前用户已经收货的商品
        Page<OrderEntity> page = getOrderByUserId(user, OrderEntity::getState, OrderState.CONFIRM_RECEIPT.name());
        List<OrderEntity> list = page.getRecords().
                stream().
                //把筛选未评价的商品
                filter(t -> OrderEntity.UN_EVALUATE.equals(t.getIsEvaluate())).toList();
        page.setTotal(list.size());
        page.setRecords(list);
        return page;
    }

    public List<OrderEntity> getDetailsByProductId(String productId) {
        return this.lambdaQuery().list().stream().filter(t -> t.getProductDetail().getId().equals(productId)).toList();
    }

    private OrderEntity getOne(String orderId,OrderState nextState,UserEntity user) {
        OrderEntity entity = this.lambdaQuery().eq(OrderEntity::getId, orderId).eq(OrderEntity::getUserId, user.getId()).one();
        AssertUtils.notNull(entity, "订单不存在");
        orderAction.doAction(entity.getState(), nextState, user.getRole());
        return entity;
    }

    private OrderEntity getOneByAdmin(String orderId,OrderState nextState,UserEntity user) {
        OrderEntity entity = this.lambdaQuery().eq(OrderEntity::getId, orderId).one();
        AssertUtils.notNull(entity, "订单不存在");
        orderAction.doAction(entity.getState(), nextState, user.getRole());
        return entity;
    }

    private Page<OrderEntity> getOrderByUserId(UserEntity user, SFunction<OrderEntity,?> sFunction, Object... val) {
        Page<OrderEntity> page;
        //管理员获取所有的
        if (Role.ADMIN.equals(user.getRole())) {
            page = this.lambdaQuery().in(sFunction, val).page(CommonPageRequestUtils.defaultPage());
        }else {
            page = this.lambdaQuery().eq(OrderEntity::getUserId, user.getId()).in(sFunction, val).page(CommonPageRequestUtils.defaultPage());
        }
        return page;
    }

    public Page<OrderEntity> getMyEvaluateOrder(String loginUserId) {
        return this.lambdaQuery().eq(OrderEntity::getUserId, loginUserId).
                eq(OrderEntity::getIsEvaluate, OrderEntity.EVALUATE).
                page(CommonPageRequestUtils.defaultPage());
    }
}
