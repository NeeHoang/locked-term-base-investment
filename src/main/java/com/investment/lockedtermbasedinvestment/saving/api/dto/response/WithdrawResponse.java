package com.investment.lockedtermbasedinvestment.saving.api.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record WithdrawResponse(
        Long earningId,
        BigDecimal available,
        UUID walletId,
        BigDecimal balanceAvailable,
        BigDecimal totalBalance
) {
}
