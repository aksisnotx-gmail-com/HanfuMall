package com.app.domain.comment.service;

import cn.hutool.core.bean.BeanUtil;
import com.app.domain.base.AbstractService;
import com.app.domain.comment.entity.ProductCommentEntity;
import com.app.domain.comment.mapper.ProductCommentMapper;
import com.app.domain.comment.vo.CommentVO;
import com.app.domain.order.entity.OrderEntity;
import com.app.domain.order.service.OrderService;
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

    private final OrderService orderService;

    private final UserService userService;

    public Boolean publishComment(ProductCommentEntity param, String loginUserId) {
        OrderEntity entity = orderService.getById(param.getOrderId());
        AssertUtils.assertTrue(orderService.hasOrder(loginUserId,entity.getId()), "未改买的商品无法评论");
        AssertUtils.assertTrue(entity.getIsEvaluate().equals(OrderEntity.UN_EVALUATE), "该商品已评价/未收货");
        param.setUserId(loginUserId);
        //修改顶单为已评价
        entity.setIsEvaluate(OrderEntity.EVALUATE);
        return this.save(param) && orderService.updateById(entity);
    }

    public Page<ProductCommentEntity> queryAllComment(String productId) {
        //查询商品
        List<OrderEntity> details = orderService.getDetailsByProductId(productId);
        //过滤出已评价的商品
        List<OrderEntity> evaluateOrders = details.stream().filter(t -> t.getIsEvaluate().equals(OrderEntity.EVALUATE)).toList();
        //如果评价的商品为空则表示此商品未被评价
        if (evaluateOrders.isEmpty()) {
            return new Page<>();
        }
        Page<ProductCommentEntity> page = this.lambdaQuery()
                //查询相关的评论
                .in(ProductCommentEntity::getOrderId,evaluateOrders.stream().map(OrderEntity::getId).toList())
                .page(CommonPageRequestUtils.defaultPage());
        //设置用户
        page.getRecords().forEach(t ->  t.setUser(userService.getById(t.getUserId())));
        return page;
    }

    public Boolean deleteComment(String commentId, UserEntity loginUser,String orderId) {
        ProductCommentEntity one = this.lambdaQuery()
                .eq(ProductCommentEntity::getId, commentId).one();
        AssertUtils.notNull(one, "评价不存在");

        OrderEntity entity = orderService.getById(orderId);
        entity.setIsEvaluate(OrderEntity.DELETE);

        //管理员
        if (Role.ADMIN.equals(loginUser.getRole())) {
            return this.removeById(commentId) && orderService.updateById(entity);
        }


        AssertUtils.assertTrue(one.getUserId().equals(loginUser.getId()), "无权删除");
        return this.removeById(commentId) && orderService.updateById(entity);
    }

    public Page<CommentVO> getMyEvaluate(String loginUserId) {
        Page<OrderEntity> myEvaluate = orderService.getMyEvaluateOrder(loginUserId);
        Page<CommentVO> commentVoPage = new Page<>();
        BeanUtil.copyProperties(myEvaluate,commentVoPage);
        //构建CommentVO
        List<CommentVO> list = myEvaluate.getRecords().stream().map(t -> {
            CommentVO commentVO = new CommentVO();
            commentVO.setComment(this.lambdaQuery().eq(ProductCommentEntity::getOrderId, t.getId()).one());
            commentVO.setOrder(t);
            return commentVO;
        }).toList();
        commentVoPage.setRecords(list);
        return commentVoPage;
    }
}
