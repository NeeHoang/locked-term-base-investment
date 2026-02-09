package com.investment.lockedtembasedinvestment.saving.domain.valueobject;

public record TermDays(int value) {

    public TermDays {
        if (value <= 0) {
            throw new IllegalArgumentException("Term days must be positive");
        }
    }
}
