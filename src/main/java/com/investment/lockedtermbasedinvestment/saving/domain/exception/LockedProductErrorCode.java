package com.investment.lockedtermbasedinvestment.saving.domain.exception;

import com.investment.lockedtermbasedinvestment.common.exceptionhanler.DomainErrorCode;
import org.springframework.http.HttpStatus;

public enum LockedProductErrorCode implements DomainErrorCode {

    LOCKED_PRODUCT_NOT_ACTIVE("LOCKED_PRODUCT_001", HttpStatus.CONFLICT),
    INVALID_SUBSCRIBE_AMOUNT("LOCKED_PRODUCT_002", HttpStatus.BAD_REQUEST),
    INSUFFICIENT_QUOTA("LOCKED_PRODUCT_003", HttpStatus.CONFLICT),
    NEGATIVE_QUOTA("LOCKED_PRODUCT_004", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_PRODUCT_CONFIG("LOCKED_PRODUCT_005", HttpStatus.BAD_REQUEST),
    INVALID_PRODUCT_ID("LOCked_PRODUCT_006", HttpStatus.BAD_REQUEST)

    ;


    private final String code;
    private final HttpStatus httpStatus;

    LockedProductErrorCode(String code, HttpStatus httpStatus) {
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
