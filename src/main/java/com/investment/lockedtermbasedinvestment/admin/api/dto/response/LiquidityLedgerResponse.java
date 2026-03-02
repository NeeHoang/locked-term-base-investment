package com.investment.lockedtermbasedinvestment.admin.api.dto.response;

import java.math.BigDecimal;

public record LiquidityLedgerResponse(
        BigDecimal amount,
        BigDecimal availableAfter
) {
}
