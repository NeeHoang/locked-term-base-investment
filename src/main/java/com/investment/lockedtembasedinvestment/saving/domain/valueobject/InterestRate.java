package com.investment.lockedtembasedinvestment.saving.domain.valueobject;

import com.investment.lockedtembasedinvestment.common.sharekernel.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record InterestRate(BigDecimal value) {

    public InterestRate {
        if (value.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException(
                "Interest rate must be positive"
        );
    }

    public Money calculateDaily(Money principal) {

        // dailyRate = annualRate / 360
        BigDecimal dailyRate = value.divide(BigDecimal.valueOf(360), 18, RoundingMode.HALF_UP);

        BigDecimal dailyInterest = principal.amount().multiply(dailyRate);

        return Money.of(dailyInterest);
    }

    public Money calculateTotal(Money principal, TermDays termDays) {
        if (principal == null || termDays == null) {
            throw new IllegalArgumentException("principal and termDays must not be null");
        }

        BigDecimal interest = principal.amount()
                .multiply(BigDecimal.valueOf(termDays.value()))
                .divide(BigDecimal.valueOf(360), 8, RoundingMode.DOWN);

        return Money.of(interest);
    }
}
