package com.app.domain.wallet.service;

import com.app.domain.base.AbstractService;
import com.app.domain.user.entity.UserEntity;
import com.app.domain.wallet.entity.WalletEntity;
import com.app.domain.wallet.entity.WalletRecordEntity;
import com.app.domain.wallet.mapper.WalletMapper;
import com.app.toolkit.web.CommonPageRequestUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sdk.exception.GlobalException;
import com.sdk.util.asserts.AssertUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author xxl
 * @since 2024/3/25
 */
@Service
@RequiredArgsConstructor
public class WalletService extends AbstractService<WalletMapper, WalletEntity> {

    private final WalletRecordService recordService;

    public WalletEntity getOne(String userId) {
        return this.lambdaQuery().eq(WalletEntity::getUserId, userId).one();
    }

    public Boolean initWallet(String userId) {
        return this.save(WalletEntity.create(userId));
    }


    public Boolean recharge(BigDecimal decimal, UserEntity loginUser) {
        WalletEntity one = this.lambdaQuery().eq(WalletEntity::getUserId, loginUser.getId()).one();
        AssertUtils.notNull(one, "钱包不存在");
        one.setBalance(one.getBalance().add(decimal));
        //增加流水记录
        return this.updateById(one) && recordService.incomeRecord(loginUser.getId(), decimal);
    }

    public boolean expenditure(BigDecimal decimal, UserEntity loginUser) {
        WalletEntity one = this.lambdaQuery().eq(WalletEntity::getUserId, loginUser.getId()).one();
        AssertUtils.notNull(one, "钱包不存在");
        //如果减少的金额小于零表示钱包余额不足
        BigDecimal subtract = one.getBalance().subtract(decimal);
        if (subtract.compareTo(BigDecimal.ZERO) < 0) {
            throw  new GlobalException("钱包余额不足,请去汉币中心充值");
        }
        one.setBalance(subtract);
        //增加流水记录
        return this.updateById(one) && recordService.expenditureRecords(loginUser.getId(), decimal);
    }

    public WalletEntity getWallet(String loginUserId) {
        return this.lambdaQuery().eq(WalletEntity::getUserId, loginUserId).one();
    }


    public Page<WalletRecordEntity> queryWalletRecord(String userId) {
        return recordService.lambdaQuery().eq(WalletRecordEntity::getUserId, userId).page(CommonPageRequestUtils.defaultPage());
    }
}
