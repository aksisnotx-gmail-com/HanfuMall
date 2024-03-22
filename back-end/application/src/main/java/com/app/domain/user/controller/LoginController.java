package com.app.domain.user.controller;

import com.app.controller.Controller;
import com.app.domain.user.entity.UserEntity;
import com.app.domain.user.param.WeChatLoginParam;
import com.sdk.resp.RespEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xxl
 * @since 2024/3/16
 */

@Tag(name = "我的")
@RequestMapping("/auth/")
@RestController
@Validated
public class LoginController extends Controller {

    @PostMapping("/login/wechat")
    @Operation(summary = "微信小程序授权登录")
    public RespEntity<UserEntity> wechatLogin(@RequestBody WeChatLoginParam param) {
        return userLoginService.loginWithWechat(param);
    }

   /* @PostMapping("/login/")
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
    }*/

}
