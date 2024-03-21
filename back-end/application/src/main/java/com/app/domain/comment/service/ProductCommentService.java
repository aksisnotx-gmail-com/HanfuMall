package com.app.domain.comment.service;

import com.app.domain.base.AbstractService;
import com.app.domain.comment.entity.ProductCommentEntity;
import com.app.domain.comment.mapper.ProductCommentMapper;
import com.app.domain.order.service.OrderService;
import com.app.domain.product.entity.ProductDetailsEntity;
import com.app.domain.product.service.ProductDetailsService;
import com.app.domain.user.entity.UserEntity;
import com.app.domain.user.enums.Role;
import com.app.toolkit.web.CommonPageRequestUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sdk.util.asserts.AssertUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.app.domain.comment.service.ProductCommentService.CACHE_KEY;

/**
 * @author xxl
 * @since 2024/3/20
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = CACHE_KEY)
public class ProductCommentService extends AbstractService<ProductCommentMapper, ProductCommentEntity> {

    public static final String CACHE_KEY = "PRODUCT_COMMENT";

    private final ProductDetailsService detailsService;

    private final OrderService orderService;

    @CacheEvict(key = "#param.productId")
    public Boolean publishComment(ProductCommentEntity param, String loginUserId) {
        ProductDetailsEntity productDetail = detailsService.getById(param.getProductId());
        AssertUtils.notNull(productDetail, "商品不存在");
        AssertUtils.assertTrue(orderService.hasProduct(loginUserId, param.getProductId()), "未改买的商品无法评论");
        param.setUserId(loginUserId);
        param.setImgUrlListToStr(param.getCommentImgUrlList());
        return this.save(param);
    }

    @Cacheable(key = "#productId")
    public Page<ProductCommentEntity> queryAllComment(String productId) {
        Page<ProductCommentEntity> page = this.lambdaQuery()
                .eq(ProductCommentEntity::getProductId, productId)
                .page(CommonPageRequestUtils.defaultPage());
        List<ProductCommentEntity> list = page.getRecords().stream().peek(t -> {
            //把图片url json形式改为list形式
            t.setImgUrlStrToList(t.getCommentImgUrl());
        }).toList();
        page.setRecords(list);
        return page;
    }


    @Cacheable(key = "#productId")
    public Boolean deleteComment(String productId, String commentId, UserEntity loginUser) {
        ProductCommentEntity one = this.lambdaQuery()
                .eq(ProductCommentEntity::getProductId, productId)
                .eq(ProductCommentEntity::getId, commentId).one();
        AssertUtils.notNull(one, "评论不存在");

        //是管理员的话
        if (Role.ADMIN.equals(loginUser.getRole())) {
            return this.removeById(commentId);
        }

        AssertUtils.assertTrue(one.getUserId().equals(loginUser.getId()), "无权删除");
        return this.removeById(commentId);
    }
}
