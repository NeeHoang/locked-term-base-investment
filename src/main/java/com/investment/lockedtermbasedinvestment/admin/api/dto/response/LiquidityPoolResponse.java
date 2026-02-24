package com.investment.lockedtermbasedinvestment.admin.api.dto.response;

import java.math.BigDecimal;

public record LiquidityPoolResponse(
        byte[] id,
        BigDecimal totalAmount,
        BigDecimal minThreshold,
        String status
) {
}
