package com.investment.lockedtembasedinvestment.admin.domain.valueobject;

import com.github.f4b6a3.ulid.Ulid;
import com.github.f4b6a3.ulid.UlidCreator;

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

        // Defensive copy
        value = Arrays.copyOf(value, value.length);
    }

    public static LiquidityPoolId generate() {
        return new LiquidityPoolId(
                UlidCreator.getUlid().toBytes()
        );
    }

    public String toUlidString() {
        return Ulid.from(value).toString();
    }


    public static LiquidityPoolId of(byte[] value) {
        return new LiquidityPoolId(value);
    }

    public byte[] bytes() {
        return Arrays.copyOf(value, value.length);
    }
}
