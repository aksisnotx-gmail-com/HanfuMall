package com.app.controller;

import com.app.domain.file.service.FileService;
import com.app.domain.product.service.ProductDetailsService;
import com.app.domain.user.service.UserService;
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
    protected UserService userLoginService;

    @Resource
    protected ProductDetailsService productDetailsService;

    @Resource
    protected  FileService fileService;


}
