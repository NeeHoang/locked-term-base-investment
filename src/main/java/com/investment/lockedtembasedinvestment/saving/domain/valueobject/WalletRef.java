package com.investment.lockedtembasedinvestment.saving.domain.valueobject;

import java.util.Objects;

public record WalletRef(Long value) {

    public WalletRef {
        Objects.requireNonNull(value, "Wallet id must not be null");
        if (value <= 0) {
            throw new IllegalArgumentException("Wallet id must be positive");
        }
    }
}

