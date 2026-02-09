package com.investment.lockedtembasedinvestment.admin.domain.policy;

import com.investment.lockedtembasedinvestment.common.enums.LiquidityPoolStatus;
import com.investment.lockedtembasedinvestment.common.sharekernel.Money;

import java.math.BigDecimal;

public class DefaultLiquidityPoolStatusPolicy
        implements LiquidityPoolStatusPolicy {

    @Override
    public LiquidityPoolStatus evaluate(
            Money totalAmount,
            Money minThreshold
    ) {
        if (totalAmount == null || minThreshold == null) {
            throw new IllegalArgumentException("Amount and threshold must not be null");
        }

        if (totalAmount.isGreaterThanOrEqual(minThreshold)) {
            return LiquidityPoolStatus.NORMAL;
        }

        if (totalAmount.isGreaterThanOrEqual(
                minThreshold.multiply(new BigDecimal("0.5"))
        )) {
            return LiquidityPoolStatus.WARNING;
        }

        return LiquidityPoolStatus.CRITICAL;
    }
}
