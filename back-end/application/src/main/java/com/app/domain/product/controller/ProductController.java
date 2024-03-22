package com.app.domain.product.controller;

import com.app.controller.Controller;
import com.app.domain.base.Entity;
import com.app.domain.product.entity.ProductDetailsEntity;
import com.app.domain.product.enums.ProductType;
import com.app.domain.product.param.ProductDetailModifyParam;
import com.app.domain.product.param.ProductDetailParam;
import com.app.domain.product.param.ProductSizeModifyParam;
import com.app.domain.product.param.vo.ProductVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonView;
import com.sdk.resp.RespEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author xxl
 * @since 2024/3/19
 */

@Tag(name = "首页-商品")
@RequestMapping("/product")
@RestController
@Validated
public class ProductController extends Controller {

    //发布商品详情
    @PostMapping("/detail/publish")
    @Operation(summary = "发布商品")
    public RespEntity<Boolean> publishDetail(@RequestBody @Validated ProductDetailParam param) {
        return RespEntity.success(productDetailsService.publishDetail(param));
    }

    //删除商品
    @GetMapping("/detail/delete/{id}")
    @Operation(summary = "删除商品")
    public RespEntity<Boolean> delete(@PathVariable("id") String id) {
        //同时删除sku表
        return RespEntity.success(productDetailsService.deleteProductById(id));
    }

    //获取商品详情
    @GetMapping("/detail/{productId}")
    @Operation(summary = "获取商品")
    public RespEntity<ProductVO> getDetail(@PathVariable("productId") String productId) {
        return RespEntity.success(productDetailsService.getDetail(productId));
    }

    //获取所有商品详情
    @GetMapping("/detail/all")
    @Operation(summary = "获取所有商品")
    public RespEntity<Page<ProductVO>> getAllDetail() {
        return RespEntity.success(productDetailsService.getAllDetail());
    }

    //修改商品信息
    @PostMapping("/detail/modify")
    @Operation(summary = "修改商品")
    public RespEntity<Boolean> modifyDetail(@RequestBody @Validated ProductDetailModifyParam param) {
        return RespEntity.success(productDetailsService.modifyDetail(param));
    }

    //删除尺码信息
    @GetMapping("/detail/size/delete")
    @Operation(summary = "删除尺码信息")
    public RespEntity<Boolean> deleteDetailSize(@RequestParam String productId,@RequestParam String sizeId) {
        return RespEntity.success(productDetailsService.deleteDetailSize(productId,sizeId));
    }

    //修改尺码信息
    @PostMapping("/detail/size/modify")
    @Operation(summary = "修改尺码信息")
    public RespEntity<Boolean> modifyDetailSize(@RequestBody @Validated ProductSizeModifyParam param) {
        return RespEntity.success(productDetailsService.modifyDetailSize(param));
    }

    //增加尺码信息
    @PostMapping("/detail/size/add")
    @Operation(summary = "增加尺码信息")
    public RespEntity<Boolean> addDetailSize(@RequestBody @Validated ProductSizeModifyParam param) {
        return RespEntity.success(productDetailsService.addDetailSize(param));
    }

    @GetMapping("/detail/type")
    @Operation(summary = "根据类型获取所有商品")
    public RespEntity<Page<ProductVO>> getDetailByType(@RequestParam ProductType type) {
        return RespEntity.success(productDetailsService.getDetailByType(type));
    }


    @GetMapping("/detail/search")
    @Operation(summary = "根据商品名字获取搜索商品")
    public RespEntity<Page<ProductVO>> search(@RequestParam String productName) {
        return RespEntity.success(productDetailsService.search(productName));
    }
}
