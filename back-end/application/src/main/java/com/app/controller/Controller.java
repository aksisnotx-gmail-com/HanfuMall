package com.app.controller;

import com.app.domain.banner.service.BannerService;
import com.app.domain.comment.service.ProductCommentService;
import com.app.domain.discovery.service.ProductDiscoveryService;
import com.app.domain.file.service.FileService;
import com.app.domain.order.service.OrderService;
import com.app.domain.product.service.ProductDetailsService;
import com.app.domain.shippingcart.service.ShoppingCartService;
import com.app.domain.user.service.UserService;
import com.app.domain.wallet.service.WalletService;
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
    protected FileService fileService;

    @Resource
    protected ProductCommentService commentService;

    @Resource
    protected ProductDiscoveryService discoveryService;

    @Resource
    protected ShoppingCartService shoppingCartService;

    @Resource
    protected BannerService bannerService;

    @Resource
    protected OrderService orderService;

    @Resource
    protected WalletService walletService;
}
