package com.app.domain.banner.controller;

import com.app.controller.Controller;
import com.app.domain.banner.entity.BannerEntity;
import com.app.domain.base.Entity;
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
 * @since 2024/3/23
 */
@Tag(name = "首页 - 轮播图")
@RequestMapping("/product/banner")
@RestController
@Validated
public class BannerController extends Controller {

    //添加轮播图
    @PostMapping("add")
    @Operation(summary = "添加banner")
    public RespEntity<Boolean> addBanner(@RequestBody List<String> param) {
        return RespEntity.success(bannerService.addBanner(param));
    }

    //删除轮播图
    @PostMapping("delete/{bannerId}")
    @Operation(summary = "删除banner")
    public RespEntity<Boolean> deleteBanner(@PathVariable String bannerId) {
        return RespEntity.success(bannerService.deleteBanner(bannerId));
    }

    //查询banner图
    @GetMapping("query")
    @Operation(summary = "查询banner")
    public RespEntity<Page<BannerEntity>> queryBanner() {
        return RespEntity.success(bannerService.queryBanner());
    }
}
