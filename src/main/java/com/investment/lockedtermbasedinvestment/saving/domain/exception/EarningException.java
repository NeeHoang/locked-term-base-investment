package com.investment.lockedtermbasedinvestment.saving.domain.exception;

import com.investment.lockedtermbasedinvestment.common.exceptionhanler.DomainException;

public class EarningException extends DomainException {

    public EarningException(EarningErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
