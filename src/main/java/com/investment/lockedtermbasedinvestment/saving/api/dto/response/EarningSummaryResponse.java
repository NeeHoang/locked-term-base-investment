package com.investment.lockedtermbasedinvestment.saving.api.dto.response;

import java.math.BigDecimal;

public record EarningSummaryResponse(
        BigDecimal totalAvailable,
        BigDecimal totalInterest
) {
}
