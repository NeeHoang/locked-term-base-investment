package com.investment.lockedtembasedinvestment.saving.domain.valueobject;

public record EarningId(Long value) {

    public EarningId {
        if (value == null) {
            throw new IllegalArgumentException(
                    "EarningId value cannot be null"
            );
        }
    }
}
