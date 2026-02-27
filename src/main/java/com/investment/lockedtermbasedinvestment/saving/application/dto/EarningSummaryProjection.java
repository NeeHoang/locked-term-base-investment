package com.investment.lockedtermbasedinvestment.saving.application.dto;

import com.investment.lockedtermbasedinvestment.saving.api.dto.response.EarningSummaryResponse;

import java.math.BigDecimal;

public interface EarningSummaryProjection {
    BigDecimal getTotalAvailable();
    BigDecimal getTotalInterest();

    EarningSummaryResponse getEarningSummary(String walletId);
}
