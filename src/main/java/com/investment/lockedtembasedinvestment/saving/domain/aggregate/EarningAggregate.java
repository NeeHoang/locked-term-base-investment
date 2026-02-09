package com.investment.lockedtembasedinvestment.saving.domain.aggregate;

import com.investment.lockedtembasedinvestment.common.sharekernel.Money;
import com.investment.lockedtembasedinvestment.saving.domain.exception.EarningErrorCode;
import com.investment.lockedtembasedinvestment.saving.domain.exception.EarningException;
import com.investment.lockedtembasedinvestment.saving.domain.policy.PenaltyPolicy;
import com.investment.lockedtembasedinvestment.saving.domain.valueobject.*;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class EarningAggregate {

    private EarningId id;
    private final SubscriptionId subscriptionId;

    private final Money principal;
    private final TermDays termDays;
    private final Money interestPerDay;

    private HoldingDays holdingDays;
    private Progress progress;

    private Money totalInterest;
    private Money available;

    private PenaltyRate penaltyRate;
    private Money penaltyAmount;

    public EarningAggregate(
            EarningId id,
            SubscriptionId subscriptionId,
            Money principal,
            TermDays termDays,
            Money interestPerDay
    ) {
        this.id = id;
        this.subscriptionId = subscriptionId;
        this.principal = principal;
        this.termDays = termDays;
        this.interestPerDay = interestPerDay;

        this.holdingDays = HoldingDays.zero();
        this.progress = Progress.zero();

        this.totalInterest = Money.zero();
        this.available = Money.zero();

        this.penaltyRate = PenaltyRate.zero();
        this.penaltyAmount = Money.zero();

        validateInvariants();
    }

    public EarningAggregate(EarningId id,
                            SubscriptionId subscriptionId,
                            Money principal, TermDays termDays,
                            Money interestPerDay, HoldingDays holdingDays,
                            Progress progress, Money totalInterest,
                            Money available, PenaltyRate penaltyRate,
                            Money penaltyAmount) {
        this.id = id;
        this.subscriptionId = subscriptionId;
        this.principal = principal;
        this.termDays = termDays;
        this.interestPerDay = interestPerDay;
        this.holdingDays = holdingDays;
        this.progress = progress;
        this.totalInterest = totalInterest;
        this.available = available;
        this.penaltyRate = penaltyRate;
        this.penaltyAmount = penaltyAmount;
    }

    public void assignId(EarningId id) {
        if (this.id != null) {
            throw new IllegalStateException("EarningId already assigned");
        }
        this.id = id;
    }

    //Daily job
    public void accrueOneDay() {
        if (holdingDays.value() >= termDays.value()) return;

        this.holdingDays = holdingDays.increment(holdingDays.value());
        this.progress = Progress.of(holdingDays.value(), termDays.value());

        this.totalInterest = totalInterest.add(interestPerDay);
        this.available = available.add(interestPerDay);

        validateInvariants();
    }

    //User withdraw
    public Money withdraw(Money amount) {
        if (amount.isNegative() || amount.isZero()) {
            throw new EarningException(
                    "Withdraw amount must be positive",
                    EarningErrorCode.INVALID_AVAILABLE_AMOUNT
            );
        }
        if (available.isLessThan(amount)) {
            throw new EarningException(
                    "Not enough available interest",
                    EarningErrorCode.INVALID_AVAILABLE_AMOUNT
            );
        }

        this.available = available.subtract(amount);

        validateInvariants();
        return amount;
    }

    public void earlyRedeem(PenaltyPolicy policy) {

        if (holdingDays.value() >= termDays.value()) {
            throw new EarningException(
                    "Cannot early redeem after maturity",
                    EarningErrorCode.INVALID_HOLDING_DAYS
            );
        }

        this.penaltyAmount = totalInterest.multiply(penaltyRate.value());

        if (available.isGreaterThanOrEqual(penaltyAmount)) {
            this.available = available.subtract(penaltyAmount);
            this.available = available.add(principal);
        }
        else {
            // available khong du -> tru vao principal
            Money remainingPenalty = penaltyAmount.subtract(available);
            this.available = available.add(principal.subtract(remainingPenalty));
        }

        validateInvariants();
    }

    public void mature() {
        if (holdingDays.value() < termDays.value()) {
            throw new EarningException(
                    "Subscription not yet MATURED",
                    EarningErrorCode.INVALID_HOLDING_DAYS
            );
        }

        this.available = available.add(principal);

        validateInvariants();
    }

    private void validateInvariants() {

        if (subscriptionId == null) {
            throw new EarningException(
                    "Subscription reference is required",
                    EarningErrorCode.SUBSCRIPTION_REF_REQUIRED
            );
        }

        if (principal == null || principal.isZero() || principal.isNegative()) {
            throw new EarningException(
                    "Principal must be positive",
                    EarningErrorCode.INVALID_PRINCIPAL
            );
        }

        if (termDays == null || termDays.value() <= 0) {
            throw new EarningException(
                    "Term days must be positive",
                    EarningErrorCode.INVALID_TERM_DAYS
            );
        }

        if (interestPerDay == null || interestPerDay.isNegative()) {
            throw new EarningException(
                    "Interest per day cannot be negative",
                    EarningErrorCode.INVALID_INTEREST_PER_DAY
            );
        }

        if (holdingDays.value() < 0 || holdingDays.value() > termDays.value()) {
            throw new EarningException(
                    "Invalid holding days",
                    EarningErrorCode.INVALID_HOLDING_DAYS
            );
        }

        if (totalInterest.isNegative()) {
            throw new EarningException(
                    "Total interest cannot be negative",
                    EarningErrorCode.INVALID_TOTAL_INTEREST
            );
        }

        if (available.isNegative()) {
            throw new EarningException(
                    "Available amount cannot be negative",
                    EarningErrorCode.INVALID_AVAILABLE_AMOUNT
            );
        }
    }
}
