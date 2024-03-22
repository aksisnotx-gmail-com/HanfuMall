package com.app.domain.shippingcart.controller;

import com.app.controller.Controller;
import com.app.domain.base.Entity;
import com.app.domain.shippingcart.entity.ShoppingCartEntity;
import com.app.domain.user.entity.LoginUser;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonView;
import com.sdk.resp.RespEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author xxl
 * @since 2024/3/22
 */
@Tag(name = "购物车")
@RequestMapping("/shoppingCart")
@RestController
@Validated
public class ShoppingCartController extends Controller {

    //商品加入购物车
    @PostMapping("/add")
    @Operation(summary = "商品加入购物车")
    public RespEntity<Boolean> addSku(@RequestBody @JsonView(Entity.INSERT.class) @Validated(Entity.INSERT.class) ShoppingCartEntity entity) {
        return RespEntity.success(shoppingCartService.addSku(entity,LoginUser.getLoginUserId()));
    }

    //加/减购物车商品
    @Operation(summary = "加/减购物车商品")
    @GetMapping("/addOrReduce")
    @Parameter(name = "number",description = "数量,如果是加则是正数,如果是减则是负数")
    @Parameter(name = "itemId",description = "购物项ID")
    public RespEntity<Boolean> addOrReduce(@RequestParam String itemId,@RequestParam Integer number) {
        return RespEntity.success(shoppingCartService.addOrReduce(itemId,number));
    }

    //查询购物车商品
    @Operation(summary = "查询购物车商品")
    @GetMapping("/getAll")
    public RespEntity<Page<ShoppingCartEntity>> getAll() {
        return RespEntity.success(shoppingCartService.getAll(LoginUser.getLoginUserId()));
    }
}
