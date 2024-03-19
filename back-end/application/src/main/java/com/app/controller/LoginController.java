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

    @PostMapping("/login/wechat")
    @Operation(summary = "微信小程序授权登录")
    public RespEntity<UserEntity> wechatLogin(@RequestBody WeChatLoginParam param) {
        return userLoginService.loginWithWechat(param);
    }

    @PostMapping("/login/")
    @Operation(summary = "前台商家登录")
    public RespEntity<UserEntity> login(@RequestBody Object param) {
        return userLoginService.login(param);
    }

    @PostMapping("/login/")
    @Operation(summary = "前台商家注册")
    public RespEntity<UserEntity> register(@RequestBody Object param) {
        return userLoginService.register(param);
    }

    @PostMapping("/getInfo/")
    @Operation(summary = "获取个人信息")
    public RespEntity<UserEntity> getInfo() {
        return userLoginService.getInfo(param);
    }

}
