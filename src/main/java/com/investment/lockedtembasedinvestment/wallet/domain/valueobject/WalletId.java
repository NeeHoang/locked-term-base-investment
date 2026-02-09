package com.investment.lockedtembasedinvestment.wallet.domain.valueobject;

public record WalletId(Long value) {

    public WalletId {
        if (value == null)
            throw new IllegalArgumentException("WalletId cannot be null");
        if (value <= 0)
            throw new IllegalArgumentException("WalletId must be greater than 0");
    }
}
