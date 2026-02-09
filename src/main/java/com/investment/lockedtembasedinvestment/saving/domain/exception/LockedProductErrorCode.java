package com.investment.lockedtembasedinvestment.saving.domain.exception;

public enum LockedProductErrorCode {

    LOCKED_PRODUCT_NOT_ACTIVE("LOCKED_PRODUCT_001"),
    INVALID_SUBSCRIBE_AMOUNT("LOCKED_PRODUCT_002"),
    INSUFFICIENT_QUOTA("LOCKED_PRODUCT_003"),
    NEGATIVE_QUOTA("LOCKED_PRODUCT_004"),
    INVALID_PRODUCT_CONFIG("LOCKED_PRODUCT_005");

    private final String code;

    LockedProductErrorCode(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }
}
