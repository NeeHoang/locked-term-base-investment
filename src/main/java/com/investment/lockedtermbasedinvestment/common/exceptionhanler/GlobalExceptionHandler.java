package com.investment.lockedtermbasedinvestment.common.exceptionhanler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<?> handleDomainException(DomainException ex) {

        return ResponseEntity
                .status(ex.getErrorCode().httpStatus())
                .body(Map.of(
                        "errorCode", ex.getErrorCode(),
                        "message", ex.getMessage()
                ));
    }
}
