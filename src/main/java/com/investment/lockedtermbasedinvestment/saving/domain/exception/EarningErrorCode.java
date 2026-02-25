package com.investment.lockedtermbasedinvestment.saving.domain.exception;

import com.investment.lockedtermbasedinvestment.common.exceptionhanler.DomainErrorCode;
import org.springframework.http.HttpStatus;

public enum EarningErrorCode implements DomainErrorCode {

    SUBSCRIPTION_REF_REQUIRED("EARNING_001", HttpStatus.BAD_REQUEST),
    INVALID_PRINCIPAL("EARNING_002", HttpStatus.BAD_REQUEST),
    INVALID_TERM_DAYS("EARNING_003", HttpStatus.BAD_REQUEST),
    INVALID_INTEREST_PER_DAY("EARNING_004", HttpStatus.BAD_REQUEST),
    INVALID_HOLDING_DAYS("EARNING_005", HttpStatus.BAD_REQUEST),
    INVALID_TOTAL_INTEREST("EARNING_006", HttpStatus.BAD_REQUEST),
    INVALID_AVAILABLE_AMOUNT("EARNING_007", HttpStatus.BAD_REQUEST),
    EARLY_WITHDRAW_NOT_ALLOWED("EARNING_008", HttpStatus.CONFLICT),
    INVALID_PENALTY_RATE("EARNING_009", HttpStatus.BAD_REQUEST),
    INVALID_EARNING_ID("EARNING_010", HttpStatus.BAD_REQUEST)

    ;
    private final String code;
    private final HttpStatus httpStatus;

    EarningErrorCode(String code, HttpStatus httpStatus) {
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
