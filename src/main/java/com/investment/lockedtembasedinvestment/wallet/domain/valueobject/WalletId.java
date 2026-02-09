package com.investment.lockedtembasedinvestment.wallet.domain.valueobject;

import java.util.UUID;

public record WalletId(UUID value) {

    public WalletId {
        if (value == null)
            throw new IllegalArgumentException("WalletId cannot be null");
    }

    public static WalletId generate() {
        return new WalletId(UUID.randomUUID());
    }
    public static WalletId from(String value) {
        return new WalletId(UUID.fromString(value));
    }
}
