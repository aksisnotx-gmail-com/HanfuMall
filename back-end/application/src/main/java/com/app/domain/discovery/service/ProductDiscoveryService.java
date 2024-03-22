package com.app.domain.discovery.service;

import com.app.domain.base.AbstractService;
import com.app.domain.discovery.entity.DiscoveryCommentEntity;
import com.app.domain.discovery.entity.DiscoveryEntity;
import com.app.domain.discovery.mapper.ProductDiscoveryMapper;
import com.app.domain.user.entity.UserEntity;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sdk.util.asserts.AssertUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.app.domain.user.enums.Role.ADMIN;

/**
 * @author xxl
 * @since 2024/3/21
 */
@Service
@RequiredArgsConstructor
public class ProductDiscoveryService extends AbstractService<ProductDiscoveryMapper, DiscoveryEntity> {

    private final DiscoveryCommentService commentService;

    public static final String CACHE_KEY = "PRODUCT_DISCOVERY";

    public Boolean publish(DiscoveryEntity entity, String loginUserId) {
        entity.setUserId(loginUserId);
        entity.setLikes(0);
        return this.save(entity);
    }

    public Boolean deleteDiscoveryById(String discoveryId, UserEntity loginUser) {
        DiscoveryEntity entity = getByIdForCheck(discoveryId);
        //只有管理员可以直接删除
        if (ADMIN.equals(loginUser.getRole())) {
            return this.removeById(entity.getId());
        }

        AssertUtils.assertTrue(loginUser.getId().equals(entity.getUserId()), "无权删除");
        return this.lambdaUpdate().
                eq(DiscoveryEntity::getId, discoveryId).
                eq(DiscoveryEntity::getUserId, loginUser.getId()).remove();
    }


    public Boolean likeDiscovery(String discoveryId, String loginUserId) {
        DiscoveryEntity entity = getByIdForCheck(discoveryId);
        entity.setLikes(entity.getLikes() + 1);
        entity.setLikeUsers();
    }

    public Boolean cancelLike(String discoveryId, String loginUserId) {
    }

    public DiscoveryEntity getDiscoveryById(String discoveryId) {
    }

    public Page<DiscoveryEntity> getAllDiscovery() {
    }

    public Boolean commentDiscovery(DiscoveryCommentEntity entity, String loginUserId) {
    }

    public Page<DiscoveryCommentEntity> getAllComment(String discoveryId) {
    }

    private DiscoveryEntity getByIdForCheck(String id) {
        DiscoveryEntity entity = this.getById(id);
        AssertUtils.notNull(entity, "记录不存在");
        return entity;
    }
}
