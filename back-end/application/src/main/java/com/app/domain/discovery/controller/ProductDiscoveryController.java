package com.app.domain.discovery.controller;

import com.app.controller.Controller;
import com.app.domain.base.Entity;
import com.app.domain.discovery.entity.DiscoveryCommentEntity;
import com.app.domain.discovery.entity.DiscoveryEntity;
import com.app.domain.user.entity.LoginUser;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonView;
import com.sdk.resp.RespEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author xxl
 * @since 2024/3/21
 */
@Tag(name = "发现")
@RequestMapping("/discovery")
@RestController
@Validated
public class ProductDiscoveryController extends Controller {

    //发布发现
    @PostMapping("/discovery/publish")
    @Operation(summary = "发布发现")
    public RespEntity<Boolean> publish(@RequestBody @JsonView(Entity.INSERT.class) DiscoveryEntity entity) {
        return RespEntity.success(discoveryService.publish(entity, LoginUser.getLoginUserId()));
    }

    //删除发现分角色
    @GetMapping("/delete/{discoveryId}")
    @Operation(summary = "删除发现")
    public RespEntity<Boolean> delete(@PathVariable String discoveryId) {
        return RespEntity.success(discoveryService.deleteDiscoveryById(discoveryId,LoginUser.getLoginUser()));
    }

    //点赞
    @GetMapping("/like/{discoveryId}")
    @Operation(summary = "点赞")
    public RespEntity<Boolean> likeDiscovery(@PathVariable String discoveryId) {
        return RespEntity.success(discoveryService.likeDiscovery(discoveryId,LoginUser.getLoginUserId()));
    }

    //取消点赞
    @GetMapping("/cancelLike/{discoveryId}")
    @Operation(summary = "取消点赞")
    public RespEntity<Boolean> cancelLike(@PathVariable String discoveryId) {
        return RespEntity.success(discoveryService.cancelLike(discoveryId,LoginUser.getLoginUserId()));
    }

    //根据ID查询具体的评论
    @GetMapping("/get/{discoveryId}")
    @Operation(summary = "根据ID查询具体的评论")
    public RespEntity<DiscoveryEntity> getDiscoveryById(@PathVariable String discoveryId) {
        return RespEntity.success(discoveryService.getDiscoveryById(discoveryId));
    }

    //查询所有的发现
    @GetMapping("/getAll/discovery")
    @Operation(summary = "查询所有的发现")
    public RespEntity<Page<DiscoveryEntity>> getAllDiscovery() {
        return RespEntity.success(discoveryService.getAllDiscovery());
    }

    //评论发现
    @PostMapping("/comment/publish")
    @Operation(summary = "评论发现/或者回复评论")
    public RespEntity<Boolean> commentDiscovery(@RequestBody @JsonView(Entity.INSERT.class) @Validated(Entity.INSERT.class) DiscoveryCommentEntity entity) {
        return RespEntity.success(discoveryService.commentDiscovery(entity,LoginUser.getLoginUserId()));
    }

    //查询发现中的所有评论
    @GetMapping("/getAll/comment/{discoveryId}")
    @Operation(summary = "评论发现中的评论")
    public RespEntity<Page<DiscoveryCommentEntity>> getAllComment(@PathVariable String discoveryId) {
        return RespEntity.success(discoveryService.getAllComment(discoveryId));
    }
}
