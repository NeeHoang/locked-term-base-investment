package com.investment.lockedtermbasedinvestment.wallet.api.dto.request;

import java.math.BigDecimal;

public record UserWalletRequest(
   BigDecimal balanceAvailable
) {}
