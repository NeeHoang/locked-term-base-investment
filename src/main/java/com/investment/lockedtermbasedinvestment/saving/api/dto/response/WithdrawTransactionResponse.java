package com.investment.lockedtermbasedinvestment.saving.api.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

public record WithdrawTransactionResponse(
        byte[] txId,
        Long earningId,
        BigDecimal availableBefore,
        BigDecimal amount,
        BigDecimal availableAfter,
        Instant createdAt)
{
}
