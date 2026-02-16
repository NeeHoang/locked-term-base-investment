package com.investment.lockedtermbasedinvestment.saving.domain.factory;

import com.investment.lockedtermbasedinvestment.common.sharekernel.Money;
import com.investment.lockedtermbasedinvestment.saving.domain.aggregate.EarningAggregate;
import com.investment.lockedtermbasedinvestment.saving.domain.aggregate.SubscriptionAggregate;
import com.investment.lockedtermbasedinvestment.saving.domain.exception.EarningErrorCode;
import com.investment.lockedtermbasedinvestment.saving.domain.exception.EarningException;

public class EarningFactory {

    private EarningFactory() {
    }

    public static EarningAggregate createFromSubscription(
            SubscriptionAggregate subscription
    ) {

        if (subscription == null) {
            throw new EarningException(
                    EarningErrorCode.SUBSCRIPTION_REF_REQUIRED,
                    "Subscription must not be null");
        }

        Money interestPerDay =
                subscription.getInterestRate()
                        .calculateDaily(subscription.getPrincipal());

        return new EarningAggregate(
                subscription.getId(),
                subscription.getPrincipal(),
                subscription.getTermDays(),
                interestPerDay
        );
    }
}
