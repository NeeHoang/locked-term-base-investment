package com.investment.lockedtermbasedinvestment.wallet.domain.factory;

import com.investment.lockedtermbasedinvestment.common.sharekernel.Money;
import com.investment.lockedtermbasedinvestment.common.enums.WalletStatus;
import com.investment.lockedtermbasedinvestment.wallet.domain.aggregate.WalletAggregate;
import com.investment.lockedtermbasedinvestment.wallet.domain.valueobject.WalletId;

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
