package com.investment.lockedtermbasedinvestment.admin.api.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;

@Builder
public record AdminInjectionResponse(
        byte[] txId,
        BigDecimal amount,

        BigDecimal liquidityBefore,
        BigDecimal liquidityAfter,

        String note,
        Instant createdAt
) {}
