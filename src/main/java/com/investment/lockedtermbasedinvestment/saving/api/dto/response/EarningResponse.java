package com.investment.lockedtermbasedinvestment.saving.api.dto.response;

import java.math.BigDecimal;

public record EarningResponse(
        Long earningId,
        Integer termDays,
        BigDecimal principal,
        BigDecimal availableToWithdraw,
        BigDecimal accruedInterest,
        Integer holdingDays,
        BigDecimal progress
) {}