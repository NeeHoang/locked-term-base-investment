package com.investment.lockedtembasedinvestment.saving.domain.factory;

import com.investment.lockedtembasedinvestment.common.sharekernel.Money;
import com.investment.lockedtembasedinvestment.saving.domain.aggregate.SubscriptionAggregate;
import com.investment.lockedtembasedinvestment.saving.domain.valueobject.InterestRate;
import com.investment.lockedtembasedinvestment.saving.domain.valueobject.LockedProductId;
import com.investment.lockedtembasedinvestment.saving.domain.valueobject.TermDays;
import com.investment.lockedtembasedinvestment.saving.domain.valueobject.WalletRef;

import java.time.LocalDate;

public class SubscriptionFactory {

    public static SubscriptionAggregate createNew(
            WalletRef walletRef,
            LockedProductId lockedProductId,
            Money principal,
            InterestRate interestRate,
            TermDays termDays,
            LocalDate startDate
    ) {
        return new SubscriptionAggregate(
                null,
                walletRef,
                lockedProductId,
                principal,
                interestRate,
                termDays,
                startDate
        );
    }
}
