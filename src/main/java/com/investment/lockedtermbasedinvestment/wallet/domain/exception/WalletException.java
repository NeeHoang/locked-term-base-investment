package com.investment.lockedtermbasedinvestment.wallet.domain.exception;

import com.investment.lockedtermbasedinvestment.common.exceptionhanler.DomainException;

public class WalletException extends DomainException {

    public WalletException(WalletErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
