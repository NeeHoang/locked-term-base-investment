package com.investment.lockedtembasedinvestment.wallet.api.dto.response;

import java.math.BigDecimal;

public record UserWalletResponse(
   Long walletId,
   BigDecimal totalBalance,
   BigDecimal balanceAvailable,
   BigDecimal balanceFrozen,
   String status
) {}
