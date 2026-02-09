package com.investment.lockedtembasedinvestment.admin.api.dto.request;

import java.math.BigDecimal;

public record LiquidityPoolRequest(
    BigDecimal minThreshold
) {}
