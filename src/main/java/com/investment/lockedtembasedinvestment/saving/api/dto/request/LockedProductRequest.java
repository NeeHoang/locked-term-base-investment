package com.investment.lockedtembasedinvestment.saving.api.dto.request;

import java.math.BigDecimal;

public record LockedProductRequest(
        Integer termDays,
        BigDecimal interestRate,
        BigDecimal minAmount,
        BigDecimal maxAmount,
        BigDecimal totalQuota,
        String description
) {}
