package com.app.domain.discovery.service;

import com.app.domain.base.AbstractService;
import com.app.domain.discovery.entity.DiscoveryCommentEntity;
import com.app.domain.discovery.entity.DiscoveryEntity;
import com.app.domain.discovery.mapper.ProductDiscoveryMapper;
import com.app.domain.user.entity.UserEntity;
import com.app.domain.user.service.UserService;
import com.app.toolkit.web.CommonPageRequestUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sdk.util.asserts.AssertUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.app.domain.discovery.service.ProductDiscoveryService.CACHE_KEY;
import static com.app.domain.user.enums.Role.ADMIN;

/**
 * @author xxl
 * @since 2024/3/21
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = CACHE_KEY)
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

    @CacheEvict(allEntries = true)
    public Boolean publish(DiscoveryEntity entity, String loginUserId) {
        entity.setUserId(loginUserId);
        entity.setLikes(0);
        return this.save(entity);
    }

    @CacheEvict(allEntries = true)
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


    @CacheEvict(allEntries = true)
    public Boolean likeDiscovery(String discoveryId, String loginUserId) {
        DiscoveryEntity entity = getById(discoveryId);
        List<String> users = entity.getLikeUsers();
        AssertUtils.assertTrue(!users.contains(loginUserId), "已经点过赞了");
        entity.setLikes(entity.getLikes() + 1);
        users.add(loginUserId);
        entity.setLikeUsers(users);
        return updateById(entity);
    }

    @CacheEvict(allEntries = true)
    public Boolean cancelLike(String discoveryId, String loginUserId) {
        DiscoveryEntity entity = getById(discoveryId);
        List<String> users = entity.getLikeUsers();
        AssertUtils.assertTrue(users.contains(loginUserId), "不能取消点赞");
        entity.setLikes(entity.getLikes() - 1);
        users.remove(loginUserId);
        entity.setLikeUsers(users);
        return updateById(entity);
    }

    @Cacheable
    public Page<DiscoveryEntity> getAllDiscovery() {
        Page<DiscoveryEntity> page = this.page(CommonPageRequestUtils.defaultPage());
        //设置User信息
        page.getRecords().forEach(t -> t.setUser(userService.getById(t.getUserId(),false)));
        return page;
    }


    @CacheEvict(allEntries = true)
    public Boolean commentDiscovery(DiscoveryCommentEntity entity, String loginUserId) {
        entity.setIsRead(UNREAD);
        entity.setUserId(loginUserId);
        return commentService.save(entity);
    }

    @Cacheable(key = "#discoveryId")
    public DiscoveryEntity getAllComment(String discoveryId) {
        DiscoveryEntity entity = getById(discoveryId);
        //设置USER
        entity.setUser(userService.getById(entity.getUserId(),false));
        //查询顶级评论
        List<DiscoveryCommentEntity> comments = commentService.lambdaQuery().eq(DiscoveryCommentEntity::getCommentId, discoveryId).list();
        //查询回复
        comments.forEach(commentService::fillReplies);
        entity.setComments(comments);
        return entity;
    }

    @CacheEvict(allEntries = true)
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
}
