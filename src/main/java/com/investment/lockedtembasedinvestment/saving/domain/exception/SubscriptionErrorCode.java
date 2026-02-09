package com.investment.lockedtembasedinvestment.saving.domain.exception;

public enum SubscriptionErrorCode {

    WALLET_REF_REQUIRED("SUB_001"),
    LOCKED_PRODUCT_REQUIRED("SUB_002"),
    INVALID_PRINCIPAL("SUB_003"),
    INTEREST_RATE_REQUIRED("SUB_004"),
    INVALID_TERM_DAYS("SUB_005"),
    START_DATE_REQUIRED("SUB_006"),
    INVALID_MATURITY_DATE("SUB_007"),
    INVALID_TOTAL_INTEREST("SUB_008"),
    INVALID_SUBSCRIPTION_STATE("SUB_009"),
    INVALID_STATUS_TRANSITION("SUB_010");

    private final String code;

    SubscriptionErrorCode(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }
}
