package com.app.domain.product.controller;

import com.app.controller.Controller;
import com.app.domain.base.Entity;
import com.app.domain.product.entity.ProductSkuEntity;
import com.app.domain.product.entity.ProductTypeEntity;
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

import java.util.List;

/**
 * @author xxl
 * @since 2024/3/19
 */

@Tag(name = "首页 - 商品")
@RequestMapping("/product")
@RestController
@Validated
public class ProductController extends Controller {

    //发布商品详情
    @PostMapping("/detail/publish")
    @Operation(summary = "发布商品 - [修改]")
    public RespEntity<Boolean> publishDetail(@RequestBody @Validated ProductDetailParam param) {
        return RespEntity.success(productDetailsService.publishDetail(param));
    }

    //删除商品
    @GetMapping("/detail/delete/{id}")
    @Operation(summary = "删除商品")
    public RespEntity<Boolean> delete(@PathVariable String id) {
        //同时删除sku表
        return RespEntity.success(productDetailsService.deleteProductById(List.of(id)));
    }

    //获取商品详情
    @GetMapping("/detail/{productId}")
    @Operation(summary = "获取商品 - [修改]")
    public RespEntity<ProductVO> getDetail(@PathVariable("productId") String productId) {
        return RespEntity.success(productDetailsService.getDetail(productId));
    }

    //获取所有商品详情
    @GetMapping("/detail/all")
    @Operation(summary = "获取所有商品 - [修改]")
    public RespEntity<Page<ProductVO>> getAllDetail() {
        return RespEntity.success(productDetailsService.getAllDetail());
    }

    //修改商品信息
    @PostMapping("/detail/modify")
    @Operation(summary = "修改商品 - [修改]")
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
    @Operation(summary = "根据类型获取所有商品 - [修改]")
    public RespEntity<Page<ProductVO>> getDetailByType(@RequestParam String typeId) {
        return RespEntity.success(productDetailsService.getDetailByType(typeId));
    }


    @GetMapping("/detail/search")
    @Operation(summary = "根据商品名字搜索商品")
    public RespEntity<Page<ProductVO>> search(@RequestParam String productName) {
        return RespEntity.success(productDetailsService.search(productName));
    }

    @GetMapping("/specialProducts")
    @Operation(summary = "特惠商品")
    public RespEntity<Page<ProductSkuEntity>> getSpecialProducts() {
        return RespEntity.success(productDetailsService.getSpecialProducts());
    }

    @GetMapping("/recommendProducts")
    @Operation(summary = "推荐商品")
    public RespEntity<Page<ProductSkuEntity>> getRecommendProducts() {
        return RespEntity.success(productDetailsService.getRecommendProducts());
    }

    //获取所有商品类型
    @GetMapping("/type")
    @Operation(summary = "获取商品类型有ID查询具体,没有ID查询所有的 - [新增]")
    public RespEntity<List<ProductTypeEntity>> getAllType(@RequestParam(required = false) String typeId) {
        return RespEntity.success(typeService.getAllType(typeId));
    }

    //增加商品类型
    @PostMapping("/type/add")
    @Operation(summary = "增加商品类型 - [新增]")
    public RespEntity<Boolean> addType(@RequestBody @Validated(Entity.INSERT.class) @JsonView(Entity.INSERT.class) ProductTypeEntity param) {
        return RespEntity.success(typeService.addType(param));
    }

    //删除商品类型
    @GetMapping("/type/delete/{typeId}")
    @Operation(summary = "删除商品类型 - [新增]")
    public RespEntity<Boolean> deleteType(@PathVariable("typeId") String typeId) {
        return RespEntity.success(productDetailsService.deleteType(typeId));
    }

    //修改商品类型
    @PostMapping("/type/modify")
    @Operation(summary = "修改商品类型 - [新增]")
    public RespEntity<Boolean> modifyType(@RequestBody @Validated(Entity.UPDATE.class) @JsonView(Entity.UPDATE.class) ProductTypeEntity param) {
        return RespEntity.success(typeService.modifyType(param));
    }
}
