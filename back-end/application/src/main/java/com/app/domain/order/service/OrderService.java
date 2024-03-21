package com.app.domain.order.service;

import com.app.domain.base.AbstractService;
import com.app.domain.order.enmus.OrderState;
import com.app.domain.order.entity.OrderDetailsEntity;
import com.app.domain.order.entity.OrderEntity;
import com.app.domain.order.mapper.OrderMapper;
import com.app.toolkit.web.CommonPageRequestUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xxl
 * @since 2024/3/21
 */
@Service
@RequiredArgsConstructor
public class OrderService extends AbstractService<OrderMapper, OrderEntity> {

    private final OrderAction orderAction;

    private  final OrderDetailsService detailsService;

    /**
     * 兼容查询返回是List/Page
     */
    private final static long MAX_SIZE = 9999;

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
                            anyMatch(t1 -> productId.equals(t1.getProductId()))
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
}
