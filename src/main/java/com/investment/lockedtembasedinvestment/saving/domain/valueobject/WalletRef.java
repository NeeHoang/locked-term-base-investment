package com.investment.lockedtembasedinvestment.saving.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

public record WalletRef(UUID value) {

    public WalletRef {
        Objects.requireNonNull(value, "Wallet id must not be null");
    }
}

