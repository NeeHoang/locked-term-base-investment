package com.investment.lockedtembasedinvestment.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record Money(BigDecimal amount) {

    private static final int SCALE = 8;
    private static final RoundingMode ROUNDING = RoundingMode.HALF_DOWN;

    public static final Money ZERO = new Money(BigDecimal.ZERO);

    public Money {
        if (amount == null) {
            amount = BigDecimal.ZERO;
        }
        amount = amount.setScale(SCALE, ROUNDING);
    }

    public static Money of(BigDecimal amount) {
        return new Money(amount);
    }

    public static Money zero() {
        return ZERO;
    }

    public Money add(Money other) {
        requireNonNull(other);
        return new Money(this.amount.add(other.amount));
    }

    public Money subtract(Money other) {
        requireNonNull(other);
        return new Money(this.amount.subtract(other.amount));
    }

    public Money multiply(BigDecimal factor) {
        if (factor == null) {
            throw new IllegalArgumentException("Multiply factor cannot be null");
        }
        return new Money(this.amount.multiply(factor));
    }

    public Money divide(long divisor) {
        if (divisor <= 0) {
            throw new IllegalArgumentException("Divisor must be greater than zero");
        }
        return new Money(this.amount.divide(
                BigDecimal.valueOf(divisor),
                SCALE,
                ROUNDING
        ));
    }

    public boolean isGreaterThanOrEqual(Money other) {
        requireNonNull(other);
        return this.amount.compareTo(other.amount) >= 0;
    }

    /* ========= INFRASTRUCTURE SUPPORT ========= */

    public BigDecimal toBigDecimal() {
        return amount;
    }

    private void requireNonNull(Money other) {
        Objects.requireNonNull(other, "Money must not be null");
    }
}
