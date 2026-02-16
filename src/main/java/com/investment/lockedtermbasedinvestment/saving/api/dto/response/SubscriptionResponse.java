package com.investment.lockedtermbasedinvestment.saving.api.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record SubscriptionResponse(
        UUID subscriptionID,
        UUID walletId,
        Long lockedProductId,
        BigDecimal principal,
        BigDecimal interestRate,
        int termDays,
        BigDecimal totalInterest,
        LocalDate startDate,
        LocalDate maturityDate,
        String status
) {
}
