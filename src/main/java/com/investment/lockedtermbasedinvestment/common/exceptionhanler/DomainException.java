package com.investment.lockedtermbasedinvestment.common.exceptionhanler;

public abstract class DomainException extends RuntimeException {

    private final DomainErrorCode errorCode;

    protected DomainException(DomainErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public DomainErrorCode getErrorCode() {
        return errorCode;
    }
}
