package com.investment.lockedtermbasedinvestment.admin.api.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

public record LiquidityLedgerResponse(
        BigDecimal amount,
        BigDecimal availableAfter,
        Instant created_at
) {
}
