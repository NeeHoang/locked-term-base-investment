package com.investment.lockedtermbasedinvestment.saving.api.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record WithdrawTransactionResponse(
        byte[] txId,
        Long earningId,
        UUID subscriptionId,
        LocalDate date,
        BigDecimal availableBefore,
        BigDecimal amount,
        BigDecimal availableAfter,
        Instant createdAt)
{
}
