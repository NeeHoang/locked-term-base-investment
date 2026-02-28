package com.investment.lockedtermbasedinvestment.saving.api.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CompletedSubscriptionResponse(
        UUID subscriptionId,
        Integer termDays,
        BigDecimal principal,
        BigDecimal interestRate,
        BigDecimal interestEarned,
        BigDecimal finalAmount,
        LocalDate startDate,
        LocalDate completedDate,
        String status,
        BigDecimal penaltyRate,
        BigDecimal earlyRedeemRate
) {}
