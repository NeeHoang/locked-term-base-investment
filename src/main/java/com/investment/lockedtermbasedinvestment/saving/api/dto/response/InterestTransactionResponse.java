package com.investment.lockedtermbasedinvestment.saving.api.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record InterestTransactionResponse(
        byte[] txId,
        Long earningId,
        UUID subscriptionId,
        LocalDate date,
        BigDecimal interestAmount,
        Instant createdAt) {
}
