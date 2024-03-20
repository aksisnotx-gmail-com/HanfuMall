package com.app.domain.product.controller;

import com.app.controller.Controller;
import com.app.domain.product.entity.ProductDetailsEntity;
import com.app.domain.product.param.ProductDetailParam;
import com.app.domain.product.param.vo.ProductVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sdk.resp.RespEntity;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "发布商品详情")
    public RespEntity<Boolean> publishDetail(@RequestBody @Validated ProductDetailParam param) {
        return RespEntity.success(productDetailsService.publishDetail(param));
    }

    //删除商品详情
    @GetMapping("/detail/delete/{id}")
    @Operation(summary = "删除商品详情")
    public RespEntity<Boolean> delete(@PathVariable("id") String id) {
        //同时删除sku表
        return RespEntity.success(productDetailsService.removeById(id));
    }

    //获取商品详情
    @GetMapping("/detail/{productId}")
    @Operation(summary = "获取商品详情")
    public RespEntity<ProductVO> getDetail(@PathVariable("productId") String productId) {
        return RespEntity.success(productDetailsService.getDetail(productId));
    }

    //获取所有商品详情
    @GetMapping("/detail/all")
    @Operation(summary = "获取所有商品详情")
    public RespEntity<Page<ProductVO>> getAllDetail() {
        return RespEntity.success(productDetailsService.getAllDetail());
    }


}
