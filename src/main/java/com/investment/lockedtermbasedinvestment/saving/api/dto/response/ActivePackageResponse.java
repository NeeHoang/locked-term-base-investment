package com.investment.lockedtermbasedinvestment.saving.api.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ActivePackageResponse(
        UUID subscriptionId,
        Long earningId,
        Long productId,
        LocalDate startDate,
        LocalDate maturityDate,
        BigDecimal principal,
        BigDecimal interestRte,
        BigDecimal accruedInterest,
        int holdingDays,
        BigDecimal progress,
        BigDecimal available

) {
}
