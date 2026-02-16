package com.investment.lockedtermbasedinvestment.saving.domain.valueobject;

public record EarningId(Long value) {

    public EarningId {
        if (value == null) {
            throw new IllegalArgumentException(
                    "EarningId value cannot be null"
            );
        }
    }
}
