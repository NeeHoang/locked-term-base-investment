package com.investment.lockedtermbasedinvestment.saving.domain.valueobject;

import java.math.BigDecimal;

public record PenaltyRate(BigDecimal value, boolean allowed) {

    public static PenaltyRate notAllowed() {
        return new PenaltyRate(BigDecimal.ZERO, false);
    }

    public static PenaltyRate of(BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) < 0
                || value.compareTo(BigDecimal.ONE) > 0) {
            throw new IllegalArgumentException("Invalid penalty rate");
        }
        return new PenaltyRate(value, true);
    }

    public boolean isAllowed() {
        return allowed;
    }
}
