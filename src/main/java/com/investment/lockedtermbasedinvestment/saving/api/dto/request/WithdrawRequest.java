package com.investment.lockedtermbasedinvestment.saving.api.dto.request;

import java.math.BigDecimal;

public record WithdrawRequest(
        Long earningId,
        BigDecimal amount
) {
}
