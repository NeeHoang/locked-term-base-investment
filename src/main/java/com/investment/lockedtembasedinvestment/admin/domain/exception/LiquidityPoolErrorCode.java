package com.investment.lockedtembasedinvestment.admin.domain.exception;

public enum LiquidityPoolErrorCode {

    INVALID_POOL_CONFIG("LIQUIDITY_POOL_001"),
    INVALID_AMOUNT("LIQUIDITY_POOL_002"),
    INSUFFICIENT_BALANCE("LIQUIDITY_POOL_003"),
    POOL_IN_CRITICAL_STATE("LIQUIDITY_POOL_004");

    private final String code;

    LiquidityPoolErrorCode(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }
}
