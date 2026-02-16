package com.investment.lockedtermbasedinvestment.saving.domain.valueobject;

import com.investment.lockedtermbasedinvestment.common.sharekernel.Money;
import com.investment.lockedtermbasedinvestment.saving.domain.exception.SubscriptionErrorCode;
import com.investment.lockedtermbasedinvestment.saving.domain.exception.SubscriptionException;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record InterestRate(BigDecimal value) {

    public InterestRate {
        if (value.compareTo(BigDecimal.ZERO) <= 0)
            throw new SubscriptionException(
                    SubscriptionErrorCode.INTEREST_RATE_REQUIRED,
                "Interest rate must be positive"
        );
    }

    public Money calculateDaily(Money principal) {

        // dailyRate = annualRate / 360
        BigDecimal dailyRate = value.divide(BigDecimal.valueOf(360), 8, RoundingMode.HALF_UP);

        BigDecimal dailyInterest = principal.amount().multiply(dailyRate);

        return Money.of(dailyInterest);
    }

    public Money calculateTotal(Money principal, TermDays termDays) {
        if (principal == null || termDays == null) {
            throw new SubscriptionException(
                    SubscriptionErrorCode.INTEREST_RATE_REQUIRED,
                    "principal and termDays must not be null"
            );
        }

        BigDecimal interest = principal.amount()
                .multiply(value)
                .multiply(BigDecimal.valueOf(termDays.value()))
                .divide(BigDecimal.valueOf(360), 8, RoundingMode.DOWN);

        return Money.of(interest);
    }
}
