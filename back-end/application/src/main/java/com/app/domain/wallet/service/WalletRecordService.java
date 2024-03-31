package com.app.domain.wallet.service;

import com.app.domain.base.AbstractService;
import com.app.domain.wallet.entity.WalletRecordEntity;
import com.app.domain.wallet.mapper.WalletRecordMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author xxl
 * @since 2024/3/31
 */
@Service
public class WalletRecordService extends AbstractService<WalletRecordMapper, WalletRecordEntity> {


    public boolean incomeRecord(String userId, BigDecimal income) {
        return this.save(WalletRecordEntity.create(userId, income, BigDecimal.ZERO));
    }

    public boolean expenditureRecords(String userId, BigDecimal expenditure) {
        return this.save(WalletRecordEntity.create(userId, BigDecimal.ZERO, expenditure));
    }
}
