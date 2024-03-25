package com.app.domain.wallet.entity;

import com.app.domain.base.Entity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * 钱包(SysWallet)表实体类
 *
 * @author makejava
 * @since 2024-03-25 10:03:07
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "钱包")
@TableName("sys_wallet")
public class WalletEntity extends Entity {
    @Serial
    private static final long serialVersionUID = 4269575420133022580L;

    //余额
    private BigDecimal balance;

    //用户ID
    private String userId;

    public static WalletEntity create(String userId) {
        WalletEntity entity = new WalletEntity();
        entity.setBalance(BigDecimal.ZERO);
        entity.setUserId(userId);
        return entity;
    }
}

