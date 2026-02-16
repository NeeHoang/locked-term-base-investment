package com.investment.lockedtermbasedinvestment.saving.domain.aggregate;

import com.investment.lockedtermbasedinvestment.common.enums.SubscriptionStatus;
import com.investment.lockedtermbasedinvestment.common.sharekernel.Money;
import com.investment.lockedtermbasedinvestment.saving.domain.exception.SubscriptionErrorCode;
import com.investment.lockedtermbasedinvestment.saving.domain.exception.SubscriptionException;
import com.investment.lockedtermbasedinvestment.saving.domain.valueobject.*;
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

    private void validateInvariants() {

        if (walletRef == null) {
            throw new SubscriptionException(
                    SubscriptionErrorCode.WALLET_REF_REQUIRED,
                    "Wallet reference is required"
            );
        }

        if (lockedProductId == null) {
            throw new SubscriptionException(
                    SubscriptionErrorCode.LOCKED_PRODUCT_REQUIRED,
                    "Locked product reference is required"
            );
        }

        if (principal == null || principal.isZero() || principal.isNegative()) {
            throw new SubscriptionException(
                    SubscriptionErrorCode.INVALID_PRINCIPAL,
                    "Principal must be positive"
            );
        }

        if (interestRate == null) {
            throw new SubscriptionException(
                    SubscriptionErrorCode.INTEREST_RATE_REQUIRED,
                    "Interest rate is required"
            );
        }

        if (termDays == null || termDays.value() <= 0) {
            throw new SubscriptionException(
                    SubscriptionErrorCode.INVALID_TERM_DAYS,
                    "Term days must be positive"
            );
        }

        if (startDate == null) {
            throw new SubscriptionException(
                    SubscriptionErrorCode.START_DATE_REQUIRED,
                    "Start date is required"
            );
        }

        if (maturityDate == null || !maturityDate.isAfter(startDate)) {
            throw new SubscriptionException(
                    SubscriptionErrorCode.INVALID_MATURITY_DATE,
                    "Maturity date must be after start date"
            );
        }

        if (totalInterest == null || totalInterest.isNegative()) {
            throw new SubscriptionException(
                    SubscriptionErrorCode.INVALID_TOTAL_INTEREST,
                    "Total interest cannot be negative"
            );
        }
    }

    public void markMatured() {
        if (status != SubscriptionStatus.ACTIVE) {
            throw new SubscriptionException(
                    SubscriptionErrorCode.INVALID_STATUS_TRANSITION,
                    "Only active subscription can be matured"
            );
        }
        this.status = SubscriptionStatus.MATURED;
    }

    public void markEarlyRedeemed() {
        if (status != SubscriptionStatus.ACTIVE) {
            throw new SubscriptionException(
                    SubscriptionErrorCode.INVALID_STATUS_TRANSITION,
                    "Only active subscription can be early redeemed"
            );
        }
        this.status = SubscriptionStatus.EARLY_REDEEMED;
    }
}

