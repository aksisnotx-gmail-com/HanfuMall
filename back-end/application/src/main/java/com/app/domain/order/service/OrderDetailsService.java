package com.app.domain.order.service;

import com.app.domain.base.AbstractService;
import com.app.domain.order.entity.OrderDetailsEntity;
import com.app.domain.order.mapper.OrderDetailsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author xxl
 * @since 2024/3/20
 */
@Service
@RequiredArgsConstructor
public class OrderDetailsService extends AbstractService<OrderDetailsMapper, OrderDetailsEntity> {


}
