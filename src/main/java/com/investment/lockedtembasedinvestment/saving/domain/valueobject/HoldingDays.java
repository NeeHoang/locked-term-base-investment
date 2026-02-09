package com.investment.lockedtembasedinvestment.saving.domain.valueobject;

public record HoldingDays(int value) {

    public static HoldingDays zero() {
        return new HoldingDays(0);
    }

    public HoldingDays {
        if (value < 0) {
            throw new IllegalArgumentException("Holding days cannot be negative");
        }
    }

    public HoldingDays increment(int value) {
        return new HoldingDays(value + 1);
    }
}
