package com.investment.lockedtermbasedinvestment.common;

public record ApiResponseDTO<T>(
        String code,
        String message,
        T data
) {

    public static <T> ApiResponseDTO<T> ok(T data) {
        return new ApiResponseDTO<>("SUCCESS", "success", data);
    }

    public static <T> ApiResponseDTO<T> created(T data) {
        return new ApiResponseDTO<>("CREATED", "Created successfully", data);
    }
}
