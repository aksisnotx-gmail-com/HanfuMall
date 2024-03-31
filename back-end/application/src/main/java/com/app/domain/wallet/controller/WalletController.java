package com.app.domain.wallet.controller;

import cn.hutool.core.util.StrUtil;
import com.app.controller.Controller;
import com.app.domain.user.entity.LoginUser;
import com.app.domain.wallet.entity.WalletEntity;
import com.app.domain.wallet.entity.WalletRecordEntity;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sdk.resp.RespEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * @author xxl
 * @since 2024/3/26
 */

@Tag(name = "我的 - 汉币中心")
@RequestMapping("/user/wallet")
@RestController
@Validated
public class WalletController extends Controller {

    //充值
    @GetMapping("/recharge")
    @Operation(summary = "充值")
    public RespEntity<Boolean> recharge(@RequestParam @Min(value = 1) BigDecimal decimal) {
        return RespEntity.success(walletService.recharge(decimal, LoginUser.getLoginUser()));
    }

    //查询
    @GetMapping("/query")
    @Operation(summary = "查询钱包余额,ID可以不传,不穿默认获取自己的 - [修改]")
    public RespEntity<WalletEntity> getWallet(@RequestParam(required = false) String userId) {
        return RespEntity.success(walletService.getWallet(StrUtil.isBlank(userId) ? LoginUser.getLoginUserId() : userId));
    }

    //查询用户的支出与收入记录
    @GetMapping("/queryRecords")
    @Operation(summary = "支出与收入记录,ID可以不传,不穿默认获取自己的 - [新增]")
    public RespEntity<Page<WalletRecordEntity>> getWalletRecords(@RequestParam(required = false) String userId) {
        return RespEntity.success(walletService.queryWalletRecord(StrUtil.isBlank(userId) ? LoginUser.getLoginUserId() : userId));
    }
}
