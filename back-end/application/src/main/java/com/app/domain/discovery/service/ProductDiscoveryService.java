package com.app.domain.discovery.service;

import com.app.domain.base.AbstractService;
import com.app.domain.discovery.entity.DiscoveryCommentEntity;
import com.app.domain.discovery.entity.DiscoveryEntity;
import com.app.domain.discovery.entity.vo.MessageListVO;
import com.app.domain.discovery.enums.CommentType;
import com.app.domain.discovery.enums.GetType;
import com.app.domain.discovery.enums.QueryType;
import com.app.domain.discovery.enums.ReadType;
import com.app.domain.discovery.mapper.ProductDiscoveryMapper;
import com.app.domain.user.entity.UserEntity;
import com.app.domain.user.service.UserService;
import com.app.toolkit.web.CommonPageRequestUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sdk.exception.GlobalException;
import com.sdk.resp.RespEntity;
import com.sdk.util.asserts.AssertUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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

    private static final String COMMENT = "comment";

    private static final String REPLY = "reply";

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
        CommentType commentType = entity.getCommentType();
        switch (commentType) {
            case COMMENT -> {
                //检查是否存在发现
                DiscoveryEntity discovery = this.getById(entity.getCommentId());
                if (entity.getUserId().equals(discovery.getUserId())) {
                    //如果对自己的评论或者回复则是已读
                    entity.setIsRead(READ);
                }
                return commentService.save(entity);
            }
            case REPLY -> {
                DiscoveryCommentEntity comment = commentService.getById(entity.getCommentId());
                if (entity.getUserId().equals(comment.getUserId())) {
                    //如果对自己的评论或者回复则是已读
                    entity.setIsRead(READ);
                }
                return commentService.save(entity);
            }
            default -> throw new GlobalException("评论类型错误");
        }
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
        List<String> unreadLikes = entity.getUnreadLikes();
        boolean match = users.parallelStream().anyMatch(t -> t.equals(loginUserId));

        //如果点赞了则取消,同时未读信息列表移出，存在就移除
        if (match) {
            entity.setLikes(entity.getLikes() - 1);
            unreadLikes.remove(loginUserId);
            users.remove(loginUserId);
            entity.setLikeUsers(users);
            return RespEntity.success("取消成功",this.updateById(entity));
        }

        //如果没有点赞则点赞，同时未读信息列表加一个用户ID
        users.add(loginUserId);
        entity.setLikeUsers(users);
        entity.setLikes(entity.getLikes() + 1);
        entity.setUnreadLikes(unreadLikes);
        if (!entity.getUserId().equals(loginUserId)) {
            //自己点赞不算未读消息
            unreadLikes.add(loginUserId);
        }
        return RespEntity.success("点赞成功",this.updateById(entity));
    }


    public MessageListVO getMsg(String loginUserId) {
        //获取未点赞的
        List<DiscoveryEntity> unreadLikes = this.lambdaQuery().
                eq(DiscoveryEntity::getUserId, loginUserId).
                list().
                stream().
                filter(t -> !t.getUnreadLikes().isEmpty()).peek(t -> {
                    List<String> unreadLikeList = t.getUnreadLikes();
                    t.setUser(userService.getById(t.getUserId()));
                    //设置未读用户信息列表
                    t.setUnreadLikeInfos(userService.list(UserEntity::getId, unreadLikeList));
                }).toList();
        //获取未读的信息：未读的信息分两种: 1. 回复自己的 2. 评论自己的
        //1. 查询所有”发现“未读评论
        List<String> discoveryIds = this.lambdaQuery().eq(DiscoveryEntity::getUserId, loginUserId).list().stream().map(DiscoveryEntity::getId).toList();
        List<DiscoveryCommentEntity> unreadComment = commentService.getUnreadComment(discoveryIds).stream().
                peek(t -> t.setUser(userService.getById(t.getUserId(),false))).toList() ;


        //2. 未读回复
        //查询自己所有的评论、回复
        List<String> replyIds = commentService.getCommentByUserId(loginUserId).stream().map(DiscoveryCommentEntity::getId).toList();
        //查询别人对我的评论、回复是否回复了
        List<DiscoveryCommentEntity> unreadReply = commentService.getUnreadReply(replyIds).stream().peek(t -> t.setUser(userService.getById(t.getUserId(),false))).toList();
        return MessageListVO.create(unreadLikes, Map.of(COMMENT,unreadComment,REPLY,unreadReply));
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean readMessage(ReadType type,List<String> ids, String loginUserId) {
        switch (type){
            case LIKE -> {
                List<DiscoveryEntity> discoveries = this.lambdaQuery().
                        eq(DiscoveryEntity::getUserId, loginUserId).
                        in(DiscoveryEntity::getId, ids).
                        list();
                //暂不校验
                //AssertUtils.assertTrue(!discoveries.isEmpty(),"只能已读自己的消息");
                //AssertUtils.assertTrue(discoveries.size() == ids.size(),"某些信息不存在,请重试");
                return this.updateBatchById(discoveries.stream().peek(t -> t.setUnreadLikes(new ArrayList<>())).toList());
            }
            case COMMENT_OR_REPLY -> {
                List<DiscoveryCommentEntity> list = commentService.lambdaQuery().in(DiscoveryCommentEntity::getId, ids).list();
                return commentService.updateBatchById(list.stream().peek(t -> t.setIsRead(READ)).toList());
            }
            default ->  {return false;}
        }
    }


    public Object getByType(QueryType type, String id) {
        switch (type){
            case DISCOVERY -> {
                DiscoveryEntity entity = this.getById(id);
                entity.setUser(userService.getById(entity.getUserId()));
                return entity;
            }
            case COMMENT -> {
                DiscoveryCommentEntity comment = commentService.getById(id);
                comment.setUser(userService.getById(comment.getUserId(),false));
                return comment;
            }
            default ->  {throw  new GlobalException("未知类型");}
        }
    }
}
