package com.app.domain.discovery.service;

import com.app.domain.base.AbstractService;
import com.app.domain.discovery.entity.DiscoveryCommentEntity;
import com.app.domain.discovery.entity.DiscoveryEntity;
import com.app.domain.discovery.enums.GetType;
import com.app.domain.discovery.mapper.ProductDiscoveryMapper;
import com.app.domain.user.entity.UserEntity;
import com.app.domain.user.service.UserService;
import com.app.toolkit.web.CommonPageRequestUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sdk.resp.RespEntity;
import com.sdk.util.asserts.AssertUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.app.domain.discovery.service.ProductDiscoveryService.CACHE_KEY;
import static com.app.domain.user.enums.Role.ADMIN;

/**
 * @author xxl
 * @since 2024/3/21
 */
@Service
@RequiredArgsConstructor
public class ProductDiscoveryService extends AbstractService<ProductDiscoveryMapper, DiscoveryEntity> {

    private final DiscoveryCommentService commentService;

    private final UserService userService;

    public static final String CACHE_KEY = "PRODUCT_DISCOVERY";

    /**
     * 已读
     */
    public static final Integer READ = 1;

    /**
     * 未读
     */
    public static final Integer UNREAD = 0;

    public Boolean publish(DiscoveryEntity entity, String loginUserId) {
        entity.setUserId(loginUserId);
        entity.setLikes(0);
        return this.save(entity);
    }

    public Boolean deleteDiscoveryById(String discoveryId, UserEntity loginUser) {
        DiscoveryEntity entity = getById(discoveryId);
        //只有管理员可以直接删除
        if (ADMIN.equals(loginUser.getRole())) {
            return this.removeById(entity.getId());
        }

        AssertUtils.assertTrue(loginUser.getId().equals(entity.getUserId()), "无权删除");
        return this.lambdaUpdate().
                eq(DiscoveryEntity::getId, discoveryId).
                eq(DiscoveryEntity::getUserId, loginUser.getId()).remove();
    }

    public Page<DiscoveryEntity> getAllDiscovery(GetType type,String loginUserId) {
        Page<DiscoveryEntity> page;
        if (GetType.ALL.equals(type)) {
            page = this.page(CommonPageRequestUtils.defaultPage());
        }else {
            page = this.lambdaQuery().eq(DiscoveryEntity::getUserId,loginUserId).page(CommonPageRequestUtils.defaultPage());
        }

        //设置User信息
        page.getRecords().forEach(t -> {
            t.setUser(userService.getById(t.getUserId(), false));
            //查询顶级评论
            t.setComments(commentService.getTopComment(t.getId()));
        });
        return page;
    }


    public Boolean commentDiscovery(DiscoveryCommentEntity entity, String loginUserId) {
        entity.setIsRead(UNREAD);
        entity.setUserId(loginUserId);
        return commentService.save(entity);
    }

    public DiscoveryEntity getAllComment(String discoveryId) {
        DiscoveryEntity entity = getById(discoveryId);
        //设置USER
        entity.setUser(userService.getById(entity.getUserId(),false));
        //查询顶级评论
        List<DiscoveryCommentEntity> comments = commentService.getTopComment(discoveryId);
        //查询回复
        comments.forEach(commentService::fillReplies);
        entity.setComments(comments);
        return entity;
    }

    public Boolean deleteCommentById(String commentId, UserEntity loginUser) {
        DiscoveryCommentEntity entity = commentService.getById(commentId);

        if (ADMIN.equals(loginUser.getRole())) {
            return commentService.removeById(commentId);
        }

        AssertUtils.assertTrue(loginUser.getId().equals(entity.getUserId()), "无权删除");
        return commentService.lambdaUpdate().
                eq(DiscoveryCommentEntity::getId, commentId).
                eq(DiscoveryCommentEntity::getUserId, loginUser.getId()).remove();
    }

    public RespEntity<Boolean> likeOrCancel(String discoveryId, String loginUserId) {
        DiscoveryEntity entity = getById(discoveryId);
        List<String> users = entity.getLikeUsers();
        boolean match = users.parallelStream().anyMatch(t -> t.equals(loginUserId));

        //如果点赞了则取消
        if (match) {
            entity.setLikes(entity.getLikes() - 1);
            users.remove(loginUserId);
            entity.setLikeUsers(users);
            return RespEntity.success("取消成功",this.updateById(entity));
        }

        //如果没有点赞则点赞
        users.add(loginUserId);
        entity.setLikeUsers(users);
        entity.setLikes(entity.getLikes() + 1);
        return RespEntity.success("点赞成功",this.updateById(entity));
    }
}
