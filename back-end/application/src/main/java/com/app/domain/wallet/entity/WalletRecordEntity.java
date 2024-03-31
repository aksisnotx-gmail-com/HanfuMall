package com.app.domain.wallet.entity;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.Date;

import com.app.domain.base.Entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 钱包流水记录(SysWalletRecord)表实体类
 *
 * @author makejava
 * @since 2024-03-31 14:39:27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_wallet_record")
@Schema(description = "钱包流水记录")
public class WalletRecordEntity extends Entity {

    @Serial
    private static final long serialVersionUID = 4423464844491193920L;

    //支出
    @Schema(description = "支出")
    private BigDecimal expenditure;

    //收入
    @Schema(description = "收入")
    private BigDecimal income;

    //用户Id
    @Schema(description = "用户Id")
    private String userId;

    public static WalletRecordEntity create(String userId,BigDecimal income,BigDecimal expenditure) {
        WalletRecordEntity entity = new WalletRecordEntity();
        entity.setExpenditure(expenditure);
        entity.setIncome(income);
        entity.setUserId(userId);
        return entity;
    }
}

