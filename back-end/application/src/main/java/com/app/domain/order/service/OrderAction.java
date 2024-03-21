package com.app.domain.order.service;

import com.app.domain.order.enmus.OrderState;
import com.app.domain.user.enums.Role;

/**
 * 订单操作
 *
 * @author xxl
 * @since 2024/3/21
 */
public interface OrderAction {

    /**
     *  给我一个订单、操作、角色查看是否操作
     *
     * @param curAction 当前操作
     * @param nextAction 下一个操作
     * @param role 角色
     */
    void doAction(OrderState curAction,OrderState nextAction, Role role);
}
