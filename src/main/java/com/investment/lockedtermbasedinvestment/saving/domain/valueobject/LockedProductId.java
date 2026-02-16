package com.investment.lockedtermbasedinvestment.saving.domain.valueobject;

public record LockedProductId(Long value ) {

    public LockedProductId {
        if (value == null)
            throw new IllegalArgumentException("LockedProductId must be not null");
        if (value <= 0)
            throw new IllegalArgumentException("LockedProductId must be greater than 0");
    }

    public static LockedProductId from(Long id) {
        return new LockedProductId(id);
    }
}
