package com.investment.lockedtembasedinvestment.wallet.domain.exception;

import lombok.Getter;

@Getter
public class WalletException extends RuntimeException{

    private final String code;

    public WalletException(String message, WalletErrorCode errorCode) {
        super(message);
        this.code = errorCode.code();
    }

}
