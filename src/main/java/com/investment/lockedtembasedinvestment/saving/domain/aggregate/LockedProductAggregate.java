package com.investment.lockedtembasedinvestment.saving.domain.aggregate;

import com.investment.lockedtembasedinvestment.common.sharekernel.Money;
import com.investment.lockedtembasedinvestment.common.enums.LockedProductStatus;
import com.investment.lockedtembasedinvestment.saving.domain.exception.LockedProductErrorCode;
import com.investment.lockedtembasedinvestment.saving.domain.exception.LockedProductException;
import com.investment.lockedtembasedinvestment.saving.domain.valueobject.InterestRate;
import com.investment.lockedtembasedinvestment.saving.domain.valueobject.LockedProductId;
import com.investment.lockedtembasedinvestment.saving.domain.valueobject.TermDays;
import lombok.Getter;

@Getter
public class LockedProductAggregate {

    private LockedProductId id;
    private final TermDays termDays;
    private final InterestRate interestRate;

    private final Money minAmount;
    private final Money maxAmount;

    private final Money totalQuota;
    private Money availableQuota;

    private String description;
    private LockedProductStatus status;

    public LockedProductAggregate(
            LockedProductId id,
            TermDays termDays,
            InterestRate interestRate,
            Money minAmount,
            Money maxAmount,
            Money totalQuota,
            String description
    ) {
        this.id = id;
        this.termDays = termDays;
        this.interestRate = interestRate;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.totalQuota = totalQuota;
        this.availableQuota = totalQuota;
        this.description = description;
        this.status = LockedProductStatus.ACTIVE;
        validateInvariants();
    }

    public void assignId(LockedProductId id) {
        if (this.id != null) {
            throw new IllegalStateException("LockedProductId already assigned"); }
        this.id = id;
    }

    public void allocateQuota(Money amount) {

        if (status != LockedProductStatus.ACTIVE) {
            throw new LockedProductException(
                    "Locked Product is not active",
                    LockedProductErrorCode.LOCKED_PRODUCT_NOT_ACTIVE
            );
        }

        if (amount.isNegative() || amount.isZero()) {
            throw new LockedProductException(
                    "Subscribe amount must be positive",
                    LockedProductErrorCode.INVALID_SUBSCRIBE_AMOUNT
            );
        }

        if (!isAmountWithinRange(amount)) {
            throw new LockedProductException(
                    "Subscribe amount is out of range",
                    LockedProductErrorCode.INVALID_SUBSCRIBE_AMOUNT
            );
        }

        if (!availableQuota.isGreaterThanOrEqual(amount)) {
            throw new LockedProductException(
                    "Not enough locked product quota",
                    LockedProductErrorCode.INSUFFICIENT_QUOTA
            );
        }

        this.availableQuota = this.availableQuota.subtract(amount);

        if (availableQuota.isZero()) {
            this.status = LockedProductStatus.FULLED;
        }
    }

    public boolean canSubscribe(Money amount) {
        return status == LockedProductStatus.ACTIVE
                && isAmountWithinRange(amount)
                && availableQuota.isGreaterThanOrEqual(amount);
    }

    private void validateInvariants() {
        if (availableQuota.isNegative()) {
            throw new LockedProductException(
                    "Available quota cannot be negative",
                    LockedProductErrorCode.NEGATIVE_QUOTA
            );
        }

        if (minAmount != null && maxAmount != null
                && !maxAmount.isGreaterThanOrEqual(minAmount)) {
            throw new LockedProductException(
                    "Invalid min/max amount configuration",
                    LockedProductErrorCode.INVALID_PRODUCT_CONFIG
            );
        }

        if (totalQuota == null || totalQuota.isNegative() || totalQuota.isZero()) {
            throw new LockedProductException(
                    "Total quota must be positive",
                    LockedProductErrorCode.INVALID_PRODUCT_CONFIG
            );
        }

    }

    private boolean isAmountWithinRange(Money amount) {
        if (minAmount != null && amount.isLessThan(minAmount)) return false;
        return maxAmount == null || !amount.isGreaterThan(maxAmount);
    }
}
