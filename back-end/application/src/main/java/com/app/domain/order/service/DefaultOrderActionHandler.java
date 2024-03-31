package com.app.domain.order.service;

import com.app.domain.order.enmus.OrderState;
import com.app.domain.user.enums.Role;
import com.sdk.util.asserts.AssertUtils;
import lombok.Data;
import lombok.Getter;
import org.apache.tomcat.util.digester.Rule;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.app.domain.order.enmus.OrderState.*;

/**
 * @author xxl
 * @since 2024/3/21
 */
@Component
public class DefaultOrderActionHandler implements OrderAction {

    /**
     * 动作逻辑图详细查看order文件夹下的逻辑图
     */
    private static final HashMap<OrderState,ActionHandler> ACTION_MAP; ;

    static  {
        ACTION_MAP = new HashMap<>();
        //1. 下单动作的角色，与下一层可到达的状态
        ACTION_MAP.put(PLACE_ORDER,ActionHandler.create(Role.BUYER,MAKE_PAYMENT,CLOSE_ORDER));
        //2. 付款动作的角色,，与下一层可到达的状态
        ACTION_MAP.put(MAKE_PAYMENT,ActionHandler.create(Role.BUYER,APPLY_FOR_REFUND,SHIP_ORDER));
        //2. 关闭动作的角色，与下一层可到达的状态
        ACTION_MAP.put(CLOSE_ORDER,ActionHandler.create(List.of(Role.BUYER, Role.ADMIN),DELETE_ORDER));
        //3. 发货动作的角色，与下一层可到达的状态
        ACTION_MAP.put(SHIP_ORDER,ActionHandler.create(Role.ADMIN,CONFIRM_RECEIPT,APPLY_FOR_REFUND));
        //3. 申请退款动作的角色，与下一层可到达的状态
        ACTION_MAP.put(APPLY_FOR_REFUND,ActionHandler.create(Role.BUYER,REFUND));
        //[4,5.] 退款动作的角色，与下一层可到达的状态
        ACTION_MAP.put(REFUND,ActionHandler.create(Role.ADMIN,DELETE_ORDER));
        //4. 确认收货动作的角色，与下一层可到达的状态
        ACTION_MAP.put(CONFIRM_RECEIPT,ActionHandler.create(Role.BUYER,DELETE_ORDER));
        //5. 删除订单动作的角色，与下一层可到达的状态
        ACTION_MAP.put(DELETE_ORDER,ActionHandler.create(Role.BUYER));
    }

    @Override
    public void doAction(OrderState curAction, OrderState nextAction, Role role) {
        AssertUtils.assertTrue(ACTION_MAP.get(curAction).getNextStates().contains(nextAction),"当前订单状态操作异常");
        //如果下一层状态是可执行的则查看状态中的角色
        AssertUtils.assertTrue(ACTION_MAP.get(nextAction).getCurrentHandlerRole().contains(role),"当前订单操作角色异常");
    }

    @Data
    public static class  ActionHandler {

        private List<OrderState> nextStates;

        private List<Role> currentHandlerRole;
        public static ActionHandler create(Role role,OrderState... nextStates) {
            return ActionHandler.create(List.of(role),nextStates);
        }

        public static ActionHandler create(List<Role> role,OrderState... nextStates) {
            ActionHandler actionHandler = new ActionHandler();
            actionHandler.setNextStates(nextStates.length == 0 ? new ArrayList<>() : List.of(nextStates));
            actionHandler.setCurrentHandlerRole(role);
            return actionHandler;
        }
    }
}
