package com.investment.lockedtermbasedinvestment.saving.domain.factory;

import com.investment.lockedtermbasedinvestment.common.sharekernel.Money;
import com.investment.lockedtermbasedinvestment.saving.domain.aggregate.LockedProductAggregate;
import com.investment.lockedtermbasedinvestment.saving.domain.valueobject.InterestRate;
import com.investment.lockedtermbasedinvestment.saving.domain.valueobject.TermDays;

public class LockedProductFactory {

    public static LockedProductAggregate createNew(TermDays termDays,
                                                   InterestRate interestRate,
                                                   Money minAmount,
                                                   Money maxAmount,
                                                   Money totalQuota,
                                                   String description) {
        return new LockedProductAggregate(
                termDays,
               interestRate,
               minAmount,
               maxAmount,
               totalQuota,
                description
        );
    }
}
