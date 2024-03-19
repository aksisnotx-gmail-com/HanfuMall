package com.app.domain.product.controller;

import com.app.controller.Controller;
import com.app.domain.product.param.ProductDetailParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sdk.resp.RespEntity;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author xxl
 * @since 2024/3/19
 */

@Tag(name = "商品相关接口")
@RequestMapping("/product")
@RestController
@Validated
public class ProductController extends Controller {

    //发布商品详情
    @PostMapping("/detail/publish")
    public RespEntity<Boolean> publishDetail(@RequestBody @Validated ProductDetailParam param) {
        return RespEntity.success(productDetailsService.publishDetail(param));
    }

    //删除商品详情
    @GetMapping("/detail/delete/{id}")
    public RespEntity<Boolean> publishDetail(@PathVariable("id") String id) {
        //同时删除sku表
        return RespEntity.success(productDetailsService.removeById(id));
    }

    //获取商品详情
    @GetMapping("/detail/{id}")
    public RespEntity<ProductDetailParam> getDetail(@PathVariable("id") String id) {
        return RespEntity.success(productDetailsService.getDetail(id));
    }

    //获取所有商品详情
    @GetMapping("/detail/all")
    public RespEntity<Page<ProductDetailParam>> getAllDetail() {
        String userId = null;
        return RespEntity.success(productDetailsService.getAllDetail(userId));
    }

}
