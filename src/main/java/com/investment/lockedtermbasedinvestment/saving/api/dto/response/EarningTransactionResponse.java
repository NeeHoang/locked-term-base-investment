package com.investment.lockedtermbasedinvestment.saving.api.dto.response;

import com.investment.lockedtermbasedinvestment.common.enums.EarningTransaction;
import com.investment.lockedtermbasedinvestment.common.enums.EarningTxType;

import java.math.BigDecimal;
import java.time.Instant;

public record EarningTransactionResponse(
        byte[] txId,
        Long earningId,
        EarningTxType type,
        EarningTransaction status,
        BigDecimal availableBefore,
        BigDecimal amount,
        BigDecimal availableAfter,
        Instant createdAt) {
}
