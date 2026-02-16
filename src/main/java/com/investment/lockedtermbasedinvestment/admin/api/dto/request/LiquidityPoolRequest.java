package com.investment.lockedtermbasedinvestment.admin.api.dto.request;

import java.math.BigDecimal;

public record LiquidityPoolRequest(
    BigDecimal minThreshold
) {}
