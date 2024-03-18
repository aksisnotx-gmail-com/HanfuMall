package com.app.controller;

import com.app.controller.base.Controller;
import com.app.domain.user.param.WeChatLoginParam;
import com.app.repository.entities.UserEntity;
import com.sdk.resp.RespEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author xxl
 * @since 2024/3/16
 */

@Tag(name = "用户接口")
@RequestMapping("/auth/")
@RestController
@Validated
public class LoginController extends Controller {

    @PostMapping("/wechat/login/")
    @Operation(summary = "微信小程序授权登录")
    public RespEntity<UserEntity> wechatLogin(@RequestBody WeChatLoginParam param) {
        return wechatLoginService.loginWithWechat(param);
    }

    @PostMapping("/wechat/login/")
    @Operation(summary = "前台商家登录")
    public RespEntity<UserEntity> wechatLogin(@RequestBody WeChatLoginParam param) {
        return wechatLoginService.login(param);
    }


}
