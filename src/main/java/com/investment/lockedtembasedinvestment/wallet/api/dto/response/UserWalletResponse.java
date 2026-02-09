package com.investment.lockedtembasedinvestment.wallet.api.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record UserWalletResponse(
   UUID walletId,
   BigDecimal totalBalance,
   BigDecimal balanceAvailable,
   BigDecimal balanceFrozen,
   String status
) {}
