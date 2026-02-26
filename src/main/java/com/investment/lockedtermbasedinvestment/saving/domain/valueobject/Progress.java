package com.investment.lockedtermbasedinvestment.saving.domain.valueobject;

import com.investment.lockedtermbasedinvestment.saving.domain.exception.EarningErrorCode;
import com.investment.lockedtermbasedinvestment.saving.domain.exception.EarningException;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Progress(BigDecimal value) {

    public static Progress zero() {
        return new Progress(BigDecimal.ZERO);
    }

    public static Progress of(int holdingDays, int termDays) {
        if (termDays <= 0) {
            throw new EarningException(
                    EarningErrorCode.INVALID_TERM_DAYS,
                    "termDays must be > 0");
        }

        BigDecimal progress = BigDecimal.valueOf(holdingDays)
                .divide(BigDecimal.valueOf(termDays), 4, RoundingMode.HALF_UP);

        BigDecimal capped = progress.min(BigDecimal.ONE);

        return new Progress(capped);
    }

    public boolean lessThan(BigDecimal threshold) {
        return value.compareTo(threshold) < 0;
    }
}
