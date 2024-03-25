package com.app.domain.wallet.service;

import com.app.domain.base.AbstractService;
import com.app.domain.wallet.entity.WalletEntity;
import com.app.domain.wallet.mapper.WalletMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import static com.app.domain.wallet.service.WalletService.CACHE_KEY;

/**
 * @author xxl
 * @since 2024/3/25
 */
@Service
@RequiredArgsConstructor
public class WalletService extends AbstractService<WalletMapper, WalletEntity> {

    public static final String CACHE_KEY = "WALLET_SERVICE";

    public WalletEntity getOne(String userId){
        return this.lambdaQuery().eq(WalletEntity::getUserId, userId).one();
    }

    public Boolean initWallet(String userId) {
        return this.save(WalletEntity.create(userId));
    }
}
