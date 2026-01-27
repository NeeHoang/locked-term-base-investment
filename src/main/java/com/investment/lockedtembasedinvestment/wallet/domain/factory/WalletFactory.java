package com.investment.lockedtembasedinvestment.wallet.domain.factory;

import com.investment.lockedtembasedinvestment.common.Money;
import com.investment.lockedtembasedinvestment.enums.WalletStatus;
import com.investment.lockedtembasedinvestment.wallet.domain.aggregate.WalletAggregate;

public class WalletFactory {

    public static WalletAggregate createNew(Money initialAmount) {
        return new WalletAggregate(
                null,
                initialAmount,
                initialAmount,
                Money.ZERO,
                WalletStatus.ACTIVE
        );
    }
}
