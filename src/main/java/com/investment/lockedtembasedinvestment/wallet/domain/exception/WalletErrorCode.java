package com.investment.lockedtembasedinvestment.wallet.domain.exception;

public enum WalletErrorCode {

    INVALID_AMOUNT("WALLET_005"),
    INSUFFICIENT_BALANCE("WALLET_001"), // So du khong kha dung
    WALLET_INACTIVE("WALLET_002"), // wallet bi khoa hoac chua khich hoat
    TOTAL_BALANCE_MISMATCH("WALLET_004");

    private final String code;

    WalletErrorCode(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }
}
