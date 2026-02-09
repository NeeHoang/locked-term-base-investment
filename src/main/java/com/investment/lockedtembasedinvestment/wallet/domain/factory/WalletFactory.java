package com.investment.lockedtembasedinvestment.wallet.domain.factory;

import com.investment.lockedtembasedinvestment.common.sharekernel.Money;
import com.investment.lockedtembasedinvestment.common.enums.WalletStatus;
import com.investment.lockedtembasedinvestment.wallet.domain.aggregate.WalletAggregate;
import com.investment.lockedtembasedinvestment.wallet.domain.valueobject.WalletId;

public class WalletFactory {

    public static WalletAggregate createNew(Money initialAmount) {
        return new WalletAggregate(
                WalletId.generate(),
                initialAmount,
                initialAmount,
                Money.ZERO,
                WalletStatus.ACTIVE
        );
    }
}
