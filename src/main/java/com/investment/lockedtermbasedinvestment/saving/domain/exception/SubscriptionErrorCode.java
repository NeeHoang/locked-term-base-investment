package com.investment.lockedtermbasedinvestment.saving.domain.exception;

import com.investment.lockedtermbasedinvestment.common.exceptionhanler.DomainErrorCode;
import org.springframework.http.HttpStatus;

public enum SubscriptionErrorCode implements DomainErrorCode {

    WALLET_REF_REQUIRED("SUB_001", HttpStatus.BAD_REQUEST),
    LOCKED_PRODUCT_REQUIRED("SUB_002", HttpStatus.BAD_REQUEST),
    INVALID_PRINCIPAL("SUB_003", HttpStatus.BAD_REQUEST),
    INTEREST_RATE_REQUIRED("SUB_004", HttpStatus.BAD_REQUEST),
    INVALID_TERM_DAYS("SUB_005", HttpStatus.BAD_REQUEST),
    START_DATE_REQUIRED("SUB_006", HttpStatus.BAD_REQUEST),
    INVALID_MATURITY_DATE("SUB_007", HttpStatus.BAD_REQUEST),
    INVALID_TOTAL_INTEREST("SUB_008", HttpStatus.BAD_REQUEST),
    INVALID_SUBSCRIPTION_STATE("SUB_009", HttpStatus.CONFLICT),
    INVALID_STATUS_TRANSITION("SUB_010", HttpStatus.CONFLICT),
    INVALID_WALLET_ID("SUB_011", HttpStatus.BAD_REQUEST),
    EARLY_REDEEM_NOT_ALLOWED("SUB_012", HttpStatus.BAD_REQUEST),
    INVALID_SUBSCRIPTION_ID("SUB_013", HttpStatus.BAD_REQUEST)
    ;

    private final String code;
    private final HttpStatus httpStatus;

    SubscriptionErrorCode(String code, HttpStatus httpStatus) {
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
