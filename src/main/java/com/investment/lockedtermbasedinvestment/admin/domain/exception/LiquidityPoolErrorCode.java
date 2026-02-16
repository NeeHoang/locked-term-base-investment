package com.investment.lockedtermbasedinvestment.admin.domain.exception;

import com.investment.lockedtermbasedinvestment.common.exceptionhanler.DomainErrorCode;
import org.springframework.http.HttpStatus;

public enum LiquidityPoolErrorCode implements DomainErrorCode {

    INVALID_POOL_CONFIG("LIQUIDITY_POOL_001", HttpStatus.BAD_REQUEST),
    INVALID_AMOUNT("LIQUIDITY_POOL_002", HttpStatus.BAD_REQUEST),
    INSUFFICIENT_BALANCE("LIQUIDITY_POOL_003", HttpStatus.CONFLICT),
    POOL_IN_CRITICAL_STATE("LIQUIDITY_POOL_004", HttpStatus.CONFLICT);

    private final String code;
    private final HttpStatus httpStatus;

    LiquidityPoolErrorCode(String code, HttpStatus httpStatus) {
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
