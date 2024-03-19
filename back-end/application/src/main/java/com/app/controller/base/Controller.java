package com.app.controller.base;

import com.app.domain.user.service.UserLoginServiceImpl;
import com.app.toolkit.web.IController;
import com.sdk.resp.RespEntity;
import jakarta.annotation.Resource;
import lombok.Data;

/**
 * 基础控制器
 *
 * @author xxl
 * @since 2024/2/27
 */
@Data
public class Controller {

    @Resource
    protected UserLoginServiceImpl userLoginService;


}
