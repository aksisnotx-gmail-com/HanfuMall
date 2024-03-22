package com.app.domain.shippingcart.service;

import com.app.domain.base.AbstractService;
import com.app.domain.shippingcart.entity.ShoppingCartEntity;
import com.app.domain.shippingcart.mapper.ShoppingCartMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author xxl
 * @since 2024/3/22
 */
@Service
@RequiredArgsConstructor
public class ShoppingCartService extends AbstractService<ShoppingCartMapper, ShoppingCartEntity> {

    public Boolean addSku(ShoppingCartEntity entity, String loginUserId) {
        entity.setUserId(loginUserId);
        return this.save(entity);
    }
}
