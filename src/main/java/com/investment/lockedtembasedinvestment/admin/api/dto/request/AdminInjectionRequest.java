package com.investment.lockedtembasedinvestment.admin.api.dto.request;

import java.math.BigDecimal;

public record AdminInjectionRequest(
        BigDecimal amount,
        byte[] adminId,
        String note
) {
}
