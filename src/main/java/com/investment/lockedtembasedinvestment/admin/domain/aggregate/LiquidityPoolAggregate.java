package com.investment.lockedtembasedinvestment.admin.domain.aggregate;

import com.investment.lockedtembasedinvestment.admin.domain.exception.LiquidityPoolErrorCode;
import com.investment.lockedtembasedinvestment.admin.domain.exception.LiquidityPoolException;
import com.investment.lockedtembasedinvestment.admin.domain.policy.LiquidityPoolStatusPolicy;
import com.investment.lockedtembasedinvestment.admin.domain.valueobject.LiquidityPoolId;
import com.investment.lockedtembasedinvestment.common.enums.LiquidityPoolStatus;
import com.investment.lockedtembasedinvestment.common.sharekernel.Money;
import lombok.Getter;

@Getter
public class LiquidityPoolAggregate {

    private final LiquidityPoolId id;
    private Money totalAmount;
    private final Money minThreshold;
    private LiquidityPoolStatus status;

    private final LiquidityPoolStatusPolicy statusPolicy;

    public LiquidityPoolAggregate(
            LiquidityPoolId id,
            Money totalAmount,
            Money minThreshold,
            LiquidityPoolStatusPolicy statusPolicy
    ) {
        this.id = id;
        this.totalAmount = totalAmount;
        this.minThreshold = minThreshold;
        this.statusPolicy = statusPolicy;

        validateInvariants();
        recalculateStatus();
    }

    public void debit(Money amount) {

        if (amount == null || amount.isZero() || amount.isNegative()) {
            throw new LiquidityPoolException(
                    "Debit amount must be positive",
                    LiquidityPoolErrorCode.INVALID_AMOUNT
            );
        }

        if (!totalAmount.isGreaterThanOrEqual(amount)) {
            throw new LiquidityPoolException(
                    "Insufficient liquidity pool balance",
                    LiquidityPoolErrorCode.INSUFFICIENT_BALANCE
            );
        }

        if (status == LiquidityPoolStatus.CRITICAL) {
            throw new LiquidityPoolException(
                    "Liquidity pool is in CRITICAL state",
                    LiquidityPoolErrorCode.POOL_IN_CRITICAL_STATE
            );
        }

        this.totalAmount = this.totalAmount.subtract(amount);
        recalculateStatus();
    }

    public void inject(Money amount) {

        if (amount == null || amount.isZero() || amount.isNegative()) {
            throw new LiquidityPoolException(
                    "Injected amount must be positive",
                    LiquidityPoolErrorCode.INVALID_AMOUNT
            );
        }

        this.totalAmount = this.totalAmount.add(amount);
        recalculateStatus();
    }

    private void recalculateStatus() {
        this.status = statusPolicy.evaluate(
                this.totalAmount,
                this.minThreshold
        );
    }

    private void validateInvariants() {

        if (id == null) {
            throw new LiquidityPoolException(
                    "LiquidityPoolId must not be null",
                    LiquidityPoolErrorCode.INVALID_POOL_CONFIG
            );
        }

        if (totalAmount == null || totalAmount.isNegative()) {
            throw new LiquidityPoolException(
                    "Total amount cannot be negative",
                    LiquidityPoolErrorCode.INVALID_POOL_CONFIG
            );
        }

        if (minThreshold == null || minThreshold.isZero() || minThreshold.isNegative()) {
            throw new LiquidityPoolException(
                    "Min threshold must be greater than zero",
                    LiquidityPoolErrorCode.INVALID_POOL_CONFIG
            );
        }

        if (statusPolicy == null) {
            throw new LiquidityPoolException(
                    "LiquidityPoolStatusPolicy must not be null",
                    LiquidityPoolErrorCode.INVALID_POOL_CONFIG
            );
        }
    }
}
