package com.investment.lockedtembasedinvestment.saving.domain.exception;

import lombok.Getter;

@Getter
public class SubscriptionException extends RuntimeException {

    private final String code;

    public SubscriptionException(String message, SubscriptionErrorCode errorCode) {
        super(message);
        this.code = errorCode.code();
    }
}
