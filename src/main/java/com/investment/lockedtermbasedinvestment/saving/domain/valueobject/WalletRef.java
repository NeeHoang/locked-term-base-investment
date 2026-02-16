package com.investment.lockedtermbasedinvestment.saving.domain.valueobject;

import com.investment.lockedtermbasedinvestment.saving.domain.exception.SubscriptionErrorCode;
import com.investment.lockedtermbasedinvestment.saving.domain.exception.SubscriptionException;

import java.util.Objects;
import java.util.UUID;

public record WalletRef(UUID value) {

    public WalletRef {
        Objects.requireNonNull(value, "Wallet id must not be null");
    }

    public static WalletRef from(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new SubscriptionException(
                    SubscriptionErrorCode.WALLET_REF_REQUIRED,
                    "WalletId is required"
            );
        }

        try {
            return new WalletRef(UUID.fromString(raw));
        } catch (IllegalArgumentException ex) {
            throw new SubscriptionException(
                    SubscriptionErrorCode.INVALID_WALLET_ID,
                    "Invalid WalletId: " + raw
            );
        }
    }
}

