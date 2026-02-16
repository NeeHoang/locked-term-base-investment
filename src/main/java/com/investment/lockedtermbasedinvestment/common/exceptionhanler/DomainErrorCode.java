package com.investment.lockedtermbasedinvestment.common.exceptionhanler;

import org.springframework.http.HttpStatus;

public interface DomainErrorCode {
    String code();
    HttpStatus httpStatus();
}
