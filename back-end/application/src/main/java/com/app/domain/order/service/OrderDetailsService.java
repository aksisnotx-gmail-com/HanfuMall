package com.app.domain.order.service;

import com.app.domain.base.AbstractService;
import com.app.domain.order.entity.OrderDetailsEntity;
import com.app.domain.order.mapper.OrderDetailsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author xxl
 * @since 2024/3/20
 */
@Service
@RequiredArgsConstructor
public class OrderDetailsService extends AbstractService<OrderDetailsMapper, OrderDetailsEntity> {

    public BigDecimal getProductTotalPrice(String orderId) {
        return getDetailsByOrderId(orderId).stream().map(OrderDetailsEntity::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<OrderDetailsEntity>  getDetailsByOrderId(String orderId) {
        return this.lambdaQuery().eq(OrderDetailsEntity::getOrderId, orderId).list();
    }

}
