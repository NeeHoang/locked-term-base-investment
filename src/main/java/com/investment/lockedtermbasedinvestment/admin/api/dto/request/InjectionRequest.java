package com.investment.lockedtermbasedinvestment.admin.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record InjectionRequest(

        @NotNull
        BigDecimal amount,
        //01HXQZK7M9F0A8K3R5YJ2D6V4B

        @NotBlank
        String note
) {}
