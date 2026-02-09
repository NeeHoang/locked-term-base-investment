package com.investment.lockedtembasedinvestment.saving.domain.exception;

import lombok.Getter;

@Getter
public class LockedProductException extends RuntimeException{

    private final String code;

    public LockedProductException(String message, LockedProductErrorCode errorCode) {
        super(message);
        this.code = errorCode.code();
    }
}
