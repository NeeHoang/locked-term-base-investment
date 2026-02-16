package com.investment.lockedtermbasedinvestment.admin.domain.exception;

import com.investment.lockedtermbasedinvestment.common.exceptionhanler.DomainException;

public class LiquidityPoolException extends DomainException {

    public LiquidityPoolException(LiquidityPoolErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
