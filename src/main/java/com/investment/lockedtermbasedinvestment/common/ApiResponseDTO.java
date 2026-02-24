package com.investment.lockedtermbasedinvestment.common;

public record ApiResponseDTO<T>(
        String code,
        String message,
        T data
) {

    public static <T> ApiResponseDTO<T> ok(String message, T data) {
        return new ApiResponseDTO<>("SUCCESS", message, data);
    }

    public static <T> ApiResponseDTO<T> created(String message, T data) {
        return new ApiResponseDTO<>("CREATED", message, data);
    }
}
