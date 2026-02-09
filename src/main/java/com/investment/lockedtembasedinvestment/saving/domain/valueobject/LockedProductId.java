package com.investment.lockedtembasedinvestment.saving.domain.valueobject;

public record LockedProductId(Long value ) {

    public LockedProductId {
        if (value == null)
            throw new IllegalArgumentException("LockedProductId must be not null");
        if (value <= 0)
            throw new IllegalArgumentException("LockedProductId must be greater than 0");
    }
}
