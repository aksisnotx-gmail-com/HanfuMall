package com.app.domain.discovery.service;

import com.app.domain.base.AbstractService;
import com.app.domain.discovery.entity.DiscoveryCommentEntity;
import com.app.domain.discovery.enums.CommentType;
import com.app.domain.discovery.mapper.DiscoveryCommentMapper;
import com.app.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.app.domain.discovery.service.ProductDiscoveryService.READ;
import static com.app.domain.discovery.service.ProductDiscoveryService.UNREAD;

/**
 * @author xxl
 * @since 2024/3/21
 */
@Service
@RequiredArgsConstructor
public class DiscoveryCommentService extends AbstractService<DiscoveryCommentMapper,DiscoveryCommentEntity> {

    private final UserService userService;

    public void fillReplies(DiscoveryCommentEntity entity) {
        entity.setUser(userService.getById(entity.getUserId(),false));
        List<DiscoveryCommentEntity> list = this.lambdaQuery().eq(DiscoveryCommentEntity::getCommentId, entity.getId()).list();
        entity.setReplies(list);
        if (list.isEmpty()) {
            return;
        }
        //递归调用
        list.forEach(this::fillReplies);
    }

    /**
     * 获取评论不是回复
     *
     * @param discoveryId 发现Id
     * @return 评论
     */
    public List<DiscoveryCommentEntity> getTopComment(String discoveryId) {
        return this.lambdaQuery().eq(DiscoveryCommentEntity::getCommentId, discoveryId).list();
    }

    /**
     * 获取发现未读评论
     */
    public List<DiscoveryCommentEntity> getUnreadComment(List<String> discoveryId) {
        if (discoveryId.isEmpty()) {
            return new ArrayList<>();
        }
        return this.lambdaQuery().
                eq(DiscoveryCommentEntity::getCommentType, CommentType.COMMENT).
                in(DiscoveryCommentEntity::getCommentId, discoveryId).
                eq(DiscoveryCommentEntity::getIsRead, UNREAD).list();
    }

    /**
     * 获取发现未读回复
     */
    public List<DiscoveryCommentEntity> getUnreadReply(List<String> commentId) {
        if (commentId.isEmpty()) {
            return new ArrayList<>();
        }
        return this.lambdaQuery().
                in(DiscoveryCommentEntity::getCommentId, commentId).
                eq(DiscoveryCommentEntity::getCommentType, CommentType.REPLY).
                eq(DiscoveryCommentEntity::getIsRead, UNREAD).list();
    }

    /**
     * 查看一个用户的所有的评论/回复
     */
    public List<DiscoveryCommentEntity> getCommentByUserId(String userId) {
        return this.lambdaQuery().
                eq(DiscoveryCommentEntity::getUserId, userId).list();
    }
}
