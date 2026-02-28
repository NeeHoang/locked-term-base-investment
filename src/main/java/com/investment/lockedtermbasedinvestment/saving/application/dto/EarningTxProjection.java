package com.investment.lockedtermbasedinvestment.saving.application.dto;

import com.investment.lockedtermbasedinvestment.common.enums.EarningTransaction;
import com.investment.lockedtermbasedinvestment.common.enums.EarningTxType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public interface EarningTxProjection {
    byte[] getTxId();
    Long getEarningId();
    UUID getSubscriptionId();
    EarningTxType getTxType();
    EarningTransaction getStatus();
    BigDecimal getAvailableBefore();
    BigDecimal getAmount();
    BigDecimal getAvailableAfter();
    Instant getCreatedAt();
}
