package com.investment.lockedtermbasedinvestment.admin.api.dto.request;

import java.math.BigDecimal;

public record AdminInjectionRequest(
        BigDecimal amount,
        //01HXQZK7M9F0A8K3R5YJ2D6V4B
        String note
) {
}
