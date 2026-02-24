package com.investment.lockedtermbasedinvestment.saving.domain.factory;

import com.investment.lockedtermbasedinvestment.common.sharekernel.Money;
import com.investment.lockedtermbasedinvestment.saving.domain.aggregate.SubscriptionAggregate;
import com.investment.lockedtermbasedinvestment.saving.domain.valueobject.*;

import java.time.LocalDate;

public class SubscriptionFactory {

    public static SubscriptionAggregate createNew(
            WalletRef walletRef,
            LockedProductId lockedProductId,
            Money principal,
            InterestRate interestRate,
            TermDays termDays,
            LocalDate createdDate
    ) {
        LocalDate interestStartDate = createdDate.plusDays(1);

        return new SubscriptionAggregate(
                SubscriptionId.generate(),
                walletRef,
                lockedProductId,
                principal,
                interestRate,
                termDays,
                interestStartDate
        );
    }
}
