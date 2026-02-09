package com.investment.lockedtembasedinvestment.admin.domain.valueobject;

import java.util.Arrays;
import java.util.Objects;

public record LiquidityPoolId(byte[] value) {

    public LiquidityPoolId {

        Objects.requireNonNull(value, "LiquidityPoolId value must not be null");

        if (value.length != 16) {
            throw new IllegalArgumentException(
                    "LiquidityPoolId must be exactly 16 bytes (ULID)"
            );
        }

        // Defensive copy – cực kỳ quan trọng
        value = Arrays.copyOf(value, value.length);
    }

    public byte[] bytes() {
        return Arrays.copyOf(value, value.length);
    }
}
