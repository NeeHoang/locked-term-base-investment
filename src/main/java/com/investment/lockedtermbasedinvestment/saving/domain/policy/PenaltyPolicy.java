package com.investment.lockedtermbasedinvestment.saving.domain.policy;

import com.investment.lockedtermbasedinvestment.saving.domain.exception.EarningErrorCode;
import com.investment.lockedtermbasedinvestment.saving.domain.exception.EarningException;
import com.investment.lockedtermbasedinvestment.saving.domain.valueobject.Progress;

import java.math.BigDecimal;

public class PenaltyPolicy {

    private static final BigDecimal P30 = new BigDecimal("0.30");
    private static final BigDecimal P70 = new BigDecimal("0.70");
    private static final BigDecimal P80 = new BigDecimal("0.80");
    private static final BigDecimal P90 = new BigDecimal("0.90");

    public BigDecimal penaltyRate(Progress progress) {

        // BR-15: dưới 30% thì KHÔNG ĐƯỢC RÚT
        if (progress.lessThan(P30)) {
            throw new EarningException(
                    EarningErrorCode.EARLY_WITHRAW_NOT_ALLOWED,
                    "EarlyWithdrawNotAllowed"
            );
        }

        if (progress.lessThan(P70)) return new BigDecimal("0.50");
        if (progress.lessThan(P80)) return new BigDecimal("0.30");
        if (progress.lessThan(P90)) return new BigDecimal("0.20");

        return new BigDecimal("0.10");
    }
}
