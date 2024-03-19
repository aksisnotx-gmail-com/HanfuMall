package com.app.domain.product.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xxl
 * @since 2024/3/19
 */

@Tag(name = "商品相关接口")
@RequestMapping("/auth/")
@RestController
@Validated
public class ProductController {

    //添加商品详情
    //删除商品详情

}
