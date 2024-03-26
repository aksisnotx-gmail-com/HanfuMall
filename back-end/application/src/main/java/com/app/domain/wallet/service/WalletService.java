package com.app.domain.wallet.service;

import com.app.domain.base.AbstractService;
import com.app.domain.user.entity.UserEntity;
import com.app.domain.wallet.entity.WalletEntity;
import com.app.domain.wallet.mapper.WalletMapper;
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

    public WalletEntity getOne(String userId){
        return this.lambdaQuery().eq(WalletEntity::getUserId, userId).one();
    }

    public Boolean initWallet(String userId) {
        return this.save(WalletEntity.create(userId));
    }

    public Boolean recharge(BigDecimal decimal, UserEntity loginUser) {
        WalletEntity one = this.lambdaQuery().eq(WalletEntity::getUserId, loginUser.getId()).one();
        AssertUtils.notNull(one, "钱包不存在");
        one.setBalance(one.getBalance().add(decimal));
        return this.updateById(one);
    }

    public WalletEntity getWallet(String loginUserId) {
        return this.lambdaQuery().eq(WalletEntity::getUserId, loginUserId).one();
    }
}
