package com.investment.lockedtermbasedinvestment.wallet.domain.valueobject;

import com.investment.lockedtermbasedinvestment.wallet.domain.exception.WalletErrorCode;
import com.investment.lockedtermbasedinvestment.wallet.domain.exception.WalletException;

import java.util.UUID;

public record WalletId(UUID value) {

    public WalletId {
        if (value == null) {
            throw new WalletException(
                    WalletErrorCode.INVALID_WALLET_ID,
                    "WalletId cannot be null"
            );
        }
    }

    public static WalletId generate() {
        return new WalletId(UUID.randomUUID());
    }

    public static WalletId from(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new WalletException(
                    WalletErrorCode.INVALID_WALLET_ID,
                    "WalletId is required"
            );
        }

        try {
            return new WalletId(UUID.fromString(raw));
        } catch (IllegalArgumentException ex) {
            throw new WalletException(
                    WalletErrorCode.INVALID_WALLET_ID,
                    "Invalid WalletId format: " + raw
            );
        }
    }
}
