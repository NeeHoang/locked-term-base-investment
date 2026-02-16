package com.investment.lockedtermbasedinvestment.admin.domain.aggregate;

import com.investment.lockedtermbasedinvestment.admin.domain.exception.LiquidityPoolErrorCode;
import com.investment.lockedtermbasedinvestment.admin.domain.exception.LiquidityPoolException;
import com.investment.lockedtermbasedinvestment.admin.domain.policy.LiquidityPoolStatusPolicy;
import com.investment.lockedtermbasedinvestment.admin.domain.valueobject.LiquidityPoolId;
import com.investment.lockedtermbasedinvestment.common.enums.LiquidityPoolStatus;
import com.investment.lockedtermbasedinvestment.common.sharekernel.Money;
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
                    LiquidityPoolErrorCode.INVALID_AMOUNT,
                    "Debit amount must be positive"
            );
        }

        if (!totalAmount.isGreaterThanOrEqual(amount)) {
            throw new LiquidityPoolException(
                    LiquidityPoolErrorCode.INSUFFICIENT_BALANCE,
                    "Insufficient liquidity pool balance"
            );
        }

        if (status == LiquidityPoolStatus.CRITICAL) {
            throw new LiquidityPoolException(
                    LiquidityPoolErrorCode.POOL_IN_CRITICAL_STATE,
                    "Liquidity pool is in CRITICAL state"
            );
        }

        this.totalAmount = this.totalAmount.subtract(amount);
        recalculateStatus();
    }

    public void inject(Money amount) {

        if (amount == null || amount.isZero() || amount.isNegative()) {
            throw new LiquidityPoolException(
                    LiquidityPoolErrorCode.INVALID_AMOUNT,
                    "Injected amount must be positive"
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
                    LiquidityPoolErrorCode.INVALID_POOL_CONFIG,
                    "LiquidityPoolId must not be null"
            );
        }

        if (totalAmount == null || totalAmount.isNegative()) {
            throw new LiquidityPoolException(
                    LiquidityPoolErrorCode.INVALID_POOL_CONFIG,
                    "Total amount cannot be negative"
            );
        }

        if (minThreshold == null || minThreshold.isZero() || minThreshold.isNegative()) {
            throw new LiquidityPoolException(
                    LiquidityPoolErrorCode.INVALID_POOL_CONFIG,
                    "Min threshold must be greater than zero"
            );
        }

        if (statusPolicy == null) {
            throw new LiquidityPoolException(
                    LiquidityPoolErrorCode.INVALID_POOL_CONFIG,
                    "LiquidityPoolStatusPolicy must not be null"
            );
        }
    }
}
