package com.investment.lockedtembasedinvestment.saving.domain.factory;

import com.investment.lockedtembasedinvestment.common.sharekernel.Money;
import com.investment.lockedtembasedinvestment.saving.domain.aggregate.LockedProductAggregate;
import com.investment.lockedtembasedinvestment.saving.domain.valueobject.InterestRate;
import com.investment.lockedtembasedinvestment.saving.domain.valueobject.TermDays;

public class LockedProductFactory {

    public static LockedProductAggregate createNew(TermDays termDays,
                                                   InterestRate interestRate,
                                                   Money minAmount,
                                                   Money maxAmount,
                                                   Money totalQuota,
                                                   String description) {
        return new LockedProductAggregate(
                null,
                termDays,
               interestRate,
               minAmount,
               maxAmount,
               totalQuota,
                description
        );
    }
}
