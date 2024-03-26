package com.app.domain.comment.service;

import com.app.domain.base.AbstractService;
import com.app.domain.comment.entity.ProductCommentEntity;
import com.app.domain.comment.mapper.ProductCommentMapper;
import com.app.domain.order.entity.OrderDetailsEntity;
import com.app.domain.order.service.OrderDetailsService;
import com.app.domain.order.service.OrderService;
import com.app.domain.product.entity.ProductDetailsEntity;
import com.app.domain.product.service.ProductDetailsService;
import com.app.domain.user.entity.UserEntity;
import com.app.domain.user.enums.Role;
import com.app.domain.user.service.UserService;
import com.app.toolkit.web.CommonPageRequestUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sdk.util.asserts.AssertUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xxl
 * @since 2024/3/20
 */
@Service
@RequiredArgsConstructor
public class ProductCommentService extends AbstractService<ProductCommentMapper, ProductCommentEntity> {
    private final OrderDetailsService detailsService;

    private final OrderService orderService;

    private final UserService userService;

    public Boolean publishComment(ProductCommentEntity param, String loginUserId) {
        OrderDetailsEntity entity = detailsService.getById(param.getOrderDetailId());
        AssertUtils.assertTrue(orderService.hasOrder(loginUserId, entity), "未改买的商品无法评论");
        AssertUtils.assertTrue(entity.getIsEvaluate().equals(OrderDetailsEntity.UN_EVALUATE), "该商品已评价");
        param.setUserId(loginUserId);
        //修改顶单为已评价
        entity.setIsEvaluate(OrderDetailsEntity.EVALUATE);
        return this.save(param) && detailsService.updateById(entity);
    }

    public Page<ProductCommentEntity> queryAllComment(String productId) {
        //查询商品
        List<OrderDetailsEntity> details = detailsService.getDetailsByProductId(productId);
        //过滤出已评价的商品
        List<OrderDetailsEntity> evaluateOrders = details.stream().filter(t -> t.getIsEvaluate().equals(OrderDetailsEntity.EVALUATE)).toList();
        Page<ProductCommentEntity> page = this.lambdaQuery()
                //查询相关的评论
                .in(ProductCommentEntity::getOrderDetailId, evaluateOrders.stream().map(OrderDetailsEntity::getId).toList())
                .page(CommonPageRequestUtils.defaultPage());
        List<ProductCommentEntity> list = page.getRecords().stream().peek(t ->  t.setUser(userService.getById(t.getUserId()))).toList();
        page.setRecords(list);
        return page;
    }

    public Boolean deleteComment(String commentId, UserEntity loginUser) {
        ProductCommentEntity one = this.lambdaQuery()
                .eq(ProductCommentEntity::getId, commentId).one();
        AssertUtils.notNull(one, "评论不存在");

        //管理员
        if (Role.ADMIN.equals(loginUser.getRole())) {
            return this.removeById(commentId);
        }

        AssertUtils.assertTrue(one.getUserId().equals(loginUser.getId()), "无权删除");
        return this.removeById(commentId);
    }
}
