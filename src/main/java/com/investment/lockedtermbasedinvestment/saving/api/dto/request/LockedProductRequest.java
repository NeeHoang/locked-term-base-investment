package com.investment.lockedtermbasedinvestment.saving.api.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record LockedProductRequest(
        @NotNull(message = "Term days is required")
        @Min(value = 1, message = "Term days must be at least 1")
        Integer termDays,

        @NotNull(message = "Interest rate is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Interest rate must be greater than 0")
        BigDecimal interestRate,

        @NotNull(message = "Minimum amount is required")
        @Positive(message = "Minimum amount must be positive")
        BigDecimal minAmount,

        @NotNull(message = "Maximum amount is required")
        @Positive(message = "Maximum amount must be positive")
        BigDecimal maxAmount,

        @NotNull(message = "Total quota is required")
        @PositiveOrZero(message = "Total quota cannot be negative")
        BigDecimal totalQuota,

        @NotBlank(message = "Description cannot be blank")
        @Size(max = 500, message = "Description must be under 500 characters")
        String description
) {}