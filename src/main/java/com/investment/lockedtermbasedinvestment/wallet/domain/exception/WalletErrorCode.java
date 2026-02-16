package com.investment.lockedtermbasedinvestment.wallet.domain.exception;

import com.investment.lockedtermbasedinvestment.common.exceptionhanler.DomainErrorCode;
import org.springframework.http.HttpStatus;

public enum WalletErrorCode implements DomainErrorCode {

    INSUFFICIENT_BALANCE("WALLET_001", HttpStatus.CONFLICT),
    WALLET_INACTIVE("WALLET_002", HttpStatus.BAD_REQUEST),
    TOTAL_BALANCE_MISMATCH("WALLET_004", HttpStatus.BAD_REQUEST),
    INVALID_AMOUNT("WALLET_005", HttpStatus.BAD_REQUEST),
    WALLET_NOT_FOUND("WALLET_006", HttpStatus.NOT_FOUND),
    INVALID_WALLET_ID("WALLET_007", HttpStatus.BAD_REQUEST),
    WALLET_ID_REQUIRED("WALLET_008", HttpStatus.BAD_REQUEST)
    ;

    private final String code;
    private final HttpStatus httpStatus;

    WalletErrorCode(String code, HttpStatus httpStatus) {
        this.code = code;
        this.httpStatus = httpStatus;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public HttpStatus httpStatus() {
        return httpStatus;
    }
}
