package com.investment.lockedtembasedinvestment.admin.api.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

public record AdminInjectionResponse(
        byte[] txId,
        BigDecimal amount,
        byte[] adminId,
        String note,
        Instant createdAt
) {
}
