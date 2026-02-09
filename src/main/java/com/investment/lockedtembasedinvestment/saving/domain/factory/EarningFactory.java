package com.investment.lockedtembasedinvestment.saving.domain.factory;

import com.investment.lockedtembasedinvestment.common.sharekernel.Money;
import com.investment.lockedtembasedinvestment.saving.domain.aggregate.EarningAggregate;
import com.investment.lockedtembasedinvestment.saving.domain.aggregate.SubscriptionAggregate;

public class EarningFactory {

    private EarningFactory() {
    }

    public static EarningAggregate createFromSubscription(
            SubscriptionAggregate subscription
    ) {

        if (subscription == null) {
            throw new IllegalArgumentException("Subscription must not be null");
        }

        Money interestPerDay =
                subscription.getInterestRate()
                        .calculateDaily(subscription.getPrincipal());

        return new EarningAggregate(
                null, // id sẽ được assign khi persist
                subscription.getId(),
                subscription.getPrincipal(),
                subscription.getTermDays(),
                interestPerDay
        );
    }
}
