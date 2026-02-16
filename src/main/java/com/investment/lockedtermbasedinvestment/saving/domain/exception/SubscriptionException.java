package com.investment.lockedtermbasedinvestment.saving.domain.exception;

import com.investment.lockedtermbasedinvestment.common.exceptionhanler.DomainException;

public class SubscriptionException extends DomainException {

    public SubscriptionException(SubscriptionErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
