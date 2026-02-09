package com.investment.lockedtembasedinvestment.saving.domain.exception;

import lombok.Getter;

@Getter
public class EarningException extends RuntimeException {

    private final String code;

    public EarningException(String message, EarningErrorCode errorCode) {
        super(message);
        this.code = errorCode.code();
    }
}
