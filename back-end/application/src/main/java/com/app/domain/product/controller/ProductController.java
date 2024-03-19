package com.app.domain.product.controller;

import com.app.controller.Controller;
import com.app.domain.product.param.ProductDetailParam;
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
        return RespEntity.success(productDetailsService.publishDetail());
    }

    //删除商品详情
    //获取商品详情
    //获取所有商品详情

}
