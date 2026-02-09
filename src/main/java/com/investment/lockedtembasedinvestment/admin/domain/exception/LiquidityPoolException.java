package com.investment.lockedtembasedinvestment.admin.domain.exception;

import lombok.Getter;

@Getter
public class LiquidityPoolException extends RuntimeException {

    private final String code;

    public LiquidityPoolException(String message, LiquidityPoolErrorCode errorCode) {
        super(message);
        this.code = errorCode.code();
    }
}
