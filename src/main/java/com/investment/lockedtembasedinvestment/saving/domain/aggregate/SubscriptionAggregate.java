package com.investment.lockedtembasedinvestment.saving.domain.aggregate;

import com.investment.lockedtembasedinvestment.common.enums.SubscriptionStatus;
import com.investment.lockedtembasedinvestment.common.sharekernel.Money;
import com.investment.lockedtembasedinvestment.saving.domain.exception.SubscriptionErrorCode;
import com.investment.lockedtembasedinvestment.saving.domain.exception.SubscriptionException;
import com.investment.lockedtembasedinvestment.saving.domain.valueobject.*;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class SubscriptionAggregate {

    private SubscriptionId id;
    private final WalletRef walletRef;
    private final LockedProductId lockedProductId;

    private final Money principal;
    private final InterestRate interestRate;
    private final TermDays termDays;
    private final Money totalInterest;


    private final LocalDate startDate;
    private final LocalDate maturityDate;

    private SubscriptionStatus status;

    public SubscriptionAggregate(
            SubscriptionId id,
            WalletRef walletRef,
            LockedProductId productId,
            Money principal,
            InterestRate interestRate,
            TermDays termDays,
            LocalDate startDate
    ) {
        this.id = id;
        this.walletRef = walletRef;
        this.lockedProductId = productId;
        this.principal = principal;
        this.interestRate = interestRate;
        this.termDays = termDays;
        this.startDate = startDate;

        this.maturityDate = startDate.plusDays(termDays.value());
        this.totalInterest = interestRate.calculateTotal(principal, termDays);
        this.status = SubscriptionStatus.ACTIVE;

        validateInvariants();
    }

    public void assignId(SubscriptionId id) {
        if (this.id != null) {
            throw new IllegalArgumentException(
                    "Subscription ID already assigned"
            );
        }
        this.id = id;
    }

    private void validateInvariants() {

        if (walletRef == null) {
            throw new SubscriptionException(
                    "Wallet reference is required",
                    SubscriptionErrorCode.WALLET_REF_REQUIRED
            );
        }

        if (lockedProductId == null) {
            throw new SubscriptionException(
                    "Locked product reference is required",
                    SubscriptionErrorCode.LOCKED_PRODUCT_REQUIRED
            );
        }

        if (principal == null || principal.isZero() || principal.isNegative()) {
            throw new SubscriptionException(
                    "Principal must be positive",
                    SubscriptionErrorCode.INVALID_PRINCIPAL
            );
        }

        if (interestRate == null) {
            throw new SubscriptionException(
                    "Interest rate is required",
                    SubscriptionErrorCode.INTEREST_RATE_REQUIRED
            );
        }

        if (termDays == null || termDays.value() <= 0) {
            throw new SubscriptionException(
                    "Term days must be positive",
                    SubscriptionErrorCode.INVALID_TERM_DAYS
            );
        }

        if (startDate == null) {
            throw new SubscriptionException(
                    "Start date is required",
                    SubscriptionErrorCode.START_DATE_REQUIRED
            );
        }

        if (maturityDate == null || !maturityDate.isAfter(startDate)) {
            throw new SubscriptionException(
                    "Maturity date must be after start date",
                    SubscriptionErrorCode.INVALID_MATURITY_DATE
            );
        }

        if (totalInterest == null || totalInterest.isNegative()) {
            throw new SubscriptionException(
                    "Total interest cannot be negative",
                    SubscriptionErrorCode.INVALID_TOTAL_INTEREST
            );
        }
    }

    public void markMatured() {
        if (status != SubscriptionStatus.ACTIVE) {
            throw new SubscriptionException(
                    "Only active subscription can be matured",
                    SubscriptionErrorCode.INVALID_STATUS_TRANSITION
            );
        }
        this.status = SubscriptionStatus.MATURED;
    }

    public void markEarlyRedeemed() {
        if (status != SubscriptionStatus.ACTIVE) {
            throw new SubscriptionException(
                    "Only active subscription can be early redeemed",
                    SubscriptionErrorCode.INVALID_STATUS_TRANSITION
            );
        }
        this.status = SubscriptionStatus.EARLY_REDEEMED;
    }
}

