package com.investment.lockedtermbasedinvestment.saving.domain.policy;

import com.investment.lockedtermbasedinvestment.saving.domain.valueobject.PenaltyRate;
import com.investment.lockedtermbasedinvestment.saving.domain.valueobject.Progress;

import java.math.BigDecimal;

public class PenaltyPolicy {

    private static final BigDecimal P30 = new BigDecimal("0.30");
    private static final BigDecimal P70 = new BigDecimal("0.70");
    private static final BigDecimal P80 = new BigDecimal("0.80");
    private static final BigDecimal P90 = new BigDecimal("0.90");

    public PenaltyRate penaltyRate(Progress progress) {

        if (progress.lessThan(P30)) {
            return PenaltyRate.notAllowed();
        }

        if (progress.lessThan(P70)) return PenaltyRate.of(new BigDecimal("0.50"));
        if (progress.lessThan(P80)) return PenaltyRate.of(new BigDecimal("0.30"));
        if (progress.lessThan(P90)) return PenaltyRate.of(new BigDecimal("0.20"));

        return PenaltyRate.of(new BigDecimal("0.10"));
    }
}
