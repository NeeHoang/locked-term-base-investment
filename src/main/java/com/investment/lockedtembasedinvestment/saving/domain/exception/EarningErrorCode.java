package com.investment.lockedtembasedinvestment.saving.domain.exception;

public enum EarningErrorCode {

    SUBSCRIPTION_REF_REQUIRED("EARNING_001"),
    INVALID_PRINCIPAL("EARNING_002"),
    INVALID_TERM_DAYS("EARNING_003"),
    INVALID_INTEREST_PER_DAY("EARNING_004"),
    INVALID_HOLDING_DAYS("EARNING_005"),
    INVALID_TOTAL_INTEREST("EARNING_006"),
    INVALID_AVAILABLE_AMOUNT("EARNING_007"),
    EARLY_WITHRAW_NOT_ALLOWED("EARNING_008");

    private final String code;

    EarningErrorCode(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }
}
