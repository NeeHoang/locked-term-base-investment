package com.investment.lockedtermbasedinvestment.saving.domain.exception;

import com.investment.lockedtermbasedinvestment.common.exceptionhanler.DomainException;

public class LockedProductException extends DomainException {

    public LockedProductException(LockedProductErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
