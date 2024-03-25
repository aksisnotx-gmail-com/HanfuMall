package com.app.domain.discovery.service;

import com.app.domain.base.AbstractService;
import com.app.domain.discovery.entity.DiscoveryCommentEntity;
import com.app.domain.discovery.mapper.DiscoveryCommentMapper;
import com.app.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
