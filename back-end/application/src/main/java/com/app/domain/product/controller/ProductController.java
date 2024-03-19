package com.app.domain.product.controller;

import com.app.controller.base.Controller;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xxl
 * @since 2024/3/19
 */
@Tag(name = "商品接口")
@RequestMapping("/product")
@RestController
@Validated
public class ProductController extends Controller {

    //发布商品
    //删除商品
    //获取所有商品列表

}
