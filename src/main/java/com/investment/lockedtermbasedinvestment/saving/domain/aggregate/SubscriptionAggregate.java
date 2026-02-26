package com.investment.lockedtermbasedinvestment.saving.domain.aggregate;

import com.investment.lockedtermbasedinvestment.common.enums.SubscriptionStatus;
import com.investment.lockedtermbasedinvestment.common.sharekernel.Money;
import com.investment.lockedtermbasedinvestment.saving.domain.exception.SubscriptionErrorCode;
import com.investment.lockedtermbasedinvestment.saving.domain.exception.SubscriptionException;
import com.investment.lockedtermbasedinvestment.saving.domain.policy.PenaltyPolicy;
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

    // Constructor when create
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

        this.maturityDate = startDate.plusDays(termDays.value() - 1);
        this.totalInterest = interestRate.calculateTotal(principal, termDays);
        this.status = SubscriptionStatus.ACTIVE;

        validateInvariants();
    }

    // Constructor when reconstruction into DB
    public SubscriptionAggregate(SubscriptionId id,
                                 WalletRef walletRef,
                                 LockedProductId lockedProductId,
                                 Money principal,
                                 InterestRate interestRate,
                                 TermDays termDays,
                                 Money totalInterest,
                                 LocalDate startDate,
                                 LocalDate maturityDate,
                                 SubscriptionStatus status) {
        this.id = id;
        this.walletRef = walletRef;
        this.lockedProductId = lockedProductId;
        this.principal = principal;
        this.interestRate = interestRate;
        this.termDays = termDays;
        this.totalInterest = totalInterest;
        this.startDate = startDate;
        this.maturityDate = maturityDate;
        this.status = status;
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

        if (!maturityDate.isEqual(startDate) && !maturityDate.isAfter(startDate)) {
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

    public void accrueDaily(EarningAggregate earning,
                            LocalDate today,
                            PenaltyPolicy policy
    ) {
        if (status != SubscriptionStatus.ACTIVE) {
            return;
        }

        if (today.isBefore(startDate)) {
            return;
        }

        if (today.isAfter(maturityDate)) {
            mature(earning);
            return;
        }

        earning.accrueOneDay(policy);

        if (today.isEqual(maturityDate)) {
            this.status = SubscriptionStatus.MATURED;
            earning.mature();
        }
    }

    public boolean isEligibleForDailyAccrual(LocalDate today) {
        return status == SubscriptionStatus.ACTIVE
                && !today.isBefore(startDate);
    }

    public void mature(EarningAggregate earning) {

        if (status != SubscriptionStatus.ACTIVE) {
            return;
        }
        earning.mature();
        this.status = SubscriptionStatus.MATURED;
    }

    public void earlyRedeem(EarningAggregate earning, PenaltyPolicy policy) {

        ensureActive();

        PenaltyRate penaltyRate = policy.penaltyRate(earning.getProgress());

        earning.earlyRedeem(penaltyRate);

        this.status = SubscriptionStatus.EARLY_REDEEMED;
    }

    private void ensureActive() {
        if (status != SubscriptionStatus.ACTIVE) {
            throw new SubscriptionException(
                    SubscriptionErrorCode.INVALID_STATUS_TRANSITION,
                    "Subscription is not ACTIVE"
            );
        }
    }
}

