package com.investment.lockedtermbasedinvestment.saving.domain.aggregate;

import com.investment.lockedtermbasedinvestment.common.sharekernel.Money;
import com.investment.lockedtermbasedinvestment.common.enums.LockedProductStatus;
import com.investment.lockedtermbasedinvestment.saving.domain.exception.LockedProductErrorCode;
import com.investment.lockedtermbasedinvestment.saving.domain.exception.LockedProductException;
import com.investment.lockedtermbasedinvestment.saving.domain.valueobject.InterestRate;
import com.investment.lockedtermbasedinvestment.saving.domain.valueobject.LockedProductId;
import com.investment.lockedtermbasedinvestment.saving.domain.valueobject.TermDays;
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

    // Constructor for new CREATE
    public LockedProductAggregate(
            TermDays termDays,
            InterestRate interestRate,
            Money minAmount,
            Money maxAmount,
            Money totalQuota,
            String description
    ) {
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

    // Constructor for REHYDRATE from DB
    public LockedProductAggregate(
            LockedProductId id,
            TermDays termDays,
            InterestRate interestRate,
            Money minAmount,
            Money maxAmount,
            Money totalQuota,
            Money availableQuota,
            String description,
            LockedProductStatus status
    ) {
        this.id = id;
        this.termDays = termDays;
        this.interestRate = interestRate;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.totalQuota = totalQuota;
        this.availableQuota = availableQuota;
        this.description = description;
        this.status = status;
        validateInvariants();
    }

    public void assignId(LockedProductId id) {
        if (this.id != null) {
            throw new IllegalStateException("LockedProductId already assigned");
        }
        this.id = id;
    }

    public void subscribe(Money amount) {
        validateSubscribable(amount);

        this.availableQuota = this.availableQuota.subtract(amount);

        if (availableQuota.isZero()) {
            this.status = LockedProductStatus.FULLED;
        }

        validateInvariants();
    }

    private void validateSubscribable(Money amount) {
        validateActive();
        validateAmount(amount);
        validateQuota(amount);
    }

    private void validateActive() {
        if (status != LockedProductStatus.ACTIVE) {
            throw new LockedProductException(
                    LockedProductErrorCode.LOCKED_PRODUCT_NOT_ACTIVE,
                    "Locked product is not active"
            );
        }
    }

    private void validateAmount(Money amount) {
        if (amount == null || amount.isNegative() || amount.isZero()) {
            throw new LockedProductException(
                    LockedProductErrorCode.INVALID_SUBSCRIBE_AMOUNT,
                    "Subscribe amount must be positive"
            );
        }

        if (!isAmountWithinRange(amount)) {
            throw new LockedProductException(
                    LockedProductErrorCode.INVALID_SUBSCRIBE_AMOUNT,
                    "Subscribe amount is out of allowed range"
            );
        }
    }

    private void validateQuota(Money amount) {
        if (!availableQuota.isGreaterThanOrEqual(amount)) {
            throw new LockedProductException(
                    LockedProductErrorCode.INSUFFICIENT_QUOTA,
                    "Not enough locked product quota"
            );
        }
    }

    private void validateInvariants() {
        if (availableQuota.isNegative()) {
            throw new LockedProductException(
                    LockedProductErrorCode.NEGATIVE_QUOTA,
                    "Available quota cannot be negative"
            );
        }

        if (minAmount != null && maxAmount != null
                && !maxAmount.isGreaterThanOrEqual(minAmount)) {
            throw new LockedProductException(
                    LockedProductErrorCode.INVALID_PRODUCT_CONFIG,
                    "Invalid min/max amount configuration"
            );
        }

        if (totalQuota == null || totalQuota.isNegative() || totalQuota.isZero()) {
            throw new LockedProductException(
                    LockedProductErrorCode.INVALID_PRODUCT_CONFIG,
                    "Total quota must be positive"
            );
        }
    }

    private boolean isAmountWithinRange(Money amount) {
        if (minAmount != null && amount.isLessThan(minAmount)) return false;
        return maxAmount == null || !amount.isGreaterThan(maxAmount);
    }
}
