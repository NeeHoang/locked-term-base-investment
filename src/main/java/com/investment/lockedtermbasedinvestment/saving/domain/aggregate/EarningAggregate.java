package com.investment.lockedtermbasedinvestment.saving.domain.aggregate;

import com.investment.lockedtermbasedinvestment.common.sharekernel.Money;
import com.investment.lockedtermbasedinvestment.saving.domain.exception.EarningErrorCode;
import com.investment.lockedtermbasedinvestment.saving.domain.exception.EarningException;
import com.investment.lockedtermbasedinvestment.saving.domain.policy.PenaltyPolicy;
import com.investment.lockedtermbasedinvestment.saving.domain.valueobject.*;
import lombok.Getter;

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
            SubscriptionId subscriptionId,
            Money principal,
            TermDays termDays,
            Money interestPerDay
    ) {
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
                    EarningErrorCode.INVALID_AVAILABLE_AMOUNT,
                    "Withdraw amount must be positive"
            );
        }
        if (available.isLessThan(amount)) {
            throw new EarningException(
                    EarningErrorCode.INVALID_AVAILABLE_AMOUNT,
                    "Not enough available interest"
            );
        }

        this.available = available.subtract(amount);

        validateInvariants();
        return amount;
    }

    public void earlyRedeem(PenaltyPolicy policy) {

        if (holdingDays.value() >= termDays.value()) {
            throw new EarningException(
                    EarningErrorCode.INVALID_HOLDING_DAYS,
                    "Cannot early redeem after maturity"
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
                    EarningErrorCode.INVALID_HOLDING_DAYS,
                    "Subscription not yet MATURED"
            );
        }

        this.available = available.add(principal);

        validateInvariants();
    }

    private void validateInvariants() {

        if (subscriptionId == null) {
            throw new EarningException(
                    EarningErrorCode.SUBSCRIPTION_REF_REQUIRED,
                    "Subscription reference is required"
            );
        }

        if (principal == null || principal.isZero() || principal.isNegative()) {
            throw new EarningException(
                    EarningErrorCode.INVALID_PRINCIPAL,
                    "Principal must be positive"
            );
        }

        if (termDays == null || termDays.value() <= 0) {
            throw new EarningException(
                    EarningErrorCode.INVALID_TERM_DAYS,
                    "Term days must be positive"
            );
        }

        if (interestPerDay == null || interestPerDay.isNegative()) {
            throw new EarningException(
                    EarningErrorCode.INVALID_INTEREST_PER_DAY,
                    "Interest per day cannot be negative"
            );
        }

        if (holdingDays.value() < 0 || holdingDays.value() > termDays.value()) {
            throw new EarningException(
                    EarningErrorCode.INVALID_HOLDING_DAYS,
                    "Invalid holding days"
            );
        }

        if (totalInterest.isNegative()) {
            throw new EarningException(
                    EarningErrorCode.INVALID_TOTAL_INTEREST,
                    "Total interest cannot be negative"
            );
        }

        if (available.isNegative()) {
            throw new EarningException(
                    EarningErrorCode.INVALID_AVAILABLE_AMOUNT,
                    "Available amount cannot be negative"
            );
        }
    }
}
