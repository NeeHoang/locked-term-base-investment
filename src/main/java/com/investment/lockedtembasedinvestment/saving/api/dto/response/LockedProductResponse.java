package com.investment.lockedtembasedinvestment.saving.api.dto.response;

import java.math.BigDecimal;

public record LockedProductResponse(
        Long id,
        Integer termDays,
        BigDecimal interestRate,
        BigDecimal minAmount,
        BigDecimal maxAmount,
        BigDecimal totalQuota,
        BigDecimal availableQuota,
        String status
) {}
