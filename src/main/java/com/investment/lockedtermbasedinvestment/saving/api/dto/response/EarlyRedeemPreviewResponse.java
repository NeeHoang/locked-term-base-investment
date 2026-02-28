package com.investment.lockedtermbasedinvestment.saving.api.dto.response;

import java.math.BigDecimal;

public record EarlyRedeemPreviewResponse(
        Long earningId,
        BigDecimal principal,
        BigDecimal accruedInterest,
        BigDecimal currentProgress,
        BigDecimal penaltyRate,
        BigDecimal penaltyAmount,
        BigDecimal finalReceivableAmount
) {}