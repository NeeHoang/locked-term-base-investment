package com.investment.lockedtembasedinvestment.saving.domain.valueobject;

import java.math.BigDecimal;

public record PenaltyRate(BigDecimal value) {

    public static PenaltyRate zero() {
        return new PenaltyRate(BigDecimal.ZERO);
    }

    public static PenaltyRate of(BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) < 0
                || value.compareTo(BigDecimal.ONE) > 0) {
            throw new IllegalArgumentException("Invalid penalty rate");
        }
        return new PenaltyRate(value);
    }
}

