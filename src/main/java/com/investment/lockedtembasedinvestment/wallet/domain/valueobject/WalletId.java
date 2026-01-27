package com.investment.lockedtembasedinvestment.wallet.domain.valueobject;

public record WalletId(Long value) {

    public WalletId {
        if (value == null) throw new IllegalArgumentException("WalletId cannot be null");
    }

    public Long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WalletId walletId)) return false;
        return value.equals(walletId.value);
    }

}
