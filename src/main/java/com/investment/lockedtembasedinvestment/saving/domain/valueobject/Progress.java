package com.investment.lockedtembasedinvestment.saving.domain.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Progress(BigDecimal value) {

    public static Progress zero() {
        return new Progress(BigDecimal.ZERO);
    }

    public static Progress of(int holdingDays, int termDays) {
        if (termDays <= 0) {
            throw new IllegalArgumentException("termDays must be > 0");
        }

        BigDecimal progress = BigDecimal.valueOf(holdingDays)
                .divide(BigDecimal.valueOf(termDays), 4, RoundingMode.HALF_UP);

        return new Progress(progress);
    }

    public boolean lessThan(BigDecimal threshold) {
        return value.compareTo(threshold) < 0;
    }
}
