package com.investment.lockedtermbasedinvestment.saving.api.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public record InterestTransactionResponse(
        byte[] txId,
        Long earningId,
        LocalDate date,
        BigDecimal interestAmount,
        Instant createdAt) {
}
