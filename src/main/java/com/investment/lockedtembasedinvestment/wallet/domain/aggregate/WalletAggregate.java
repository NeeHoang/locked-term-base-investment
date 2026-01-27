package com.investment.lockedtembasedinvestment.wallet.domain.aggregate;

import com.investment.lockedtembasedinvestment.common.Money;
import com.investment.lockedtembasedinvestment.enums.WalletStatus;
import com.investment.lockedtembasedinvestment.wallet.domain.valueobject.WalletId;
import lombok.Getter;

@Getter
public class WalletAggregate {

    private WalletId id;
    private Money totalBalance;
    private Money balanceAvailable;
    private Money balanceFrozen;

    private WalletStatus status;

    private void checkTotal() {
        if (totalBalance.equals(balanceAvailable.add(balanceFrozen))) {
            throw new DomainException("Total balance are not equal to the sum of available balance and frozen balance");
        }
    }

    public void lockSaving(Money amount) {
        if (!balanceAvailable.isGreaterThanOrEqual(amount)) {
            throw new DomainException("The available balance is insufficient to lock.");
        }
        this.balanceAvailable = this.balanceAvailable.subtract(amount);
        this.balanceFrozen = this.balanceFrozen.add(amount);
        checkTotal();
    }

    public WalletAggregate(WalletId id, Money totalBalance, Money balanceAvailable, Money balanceFrozen, WalletStatus status) {
        this.id = id;
        this.totalBalance = totalBalance;
        this.balanceAvailable = balanceAvailable;
        this.balanceFrozen = balanceFrozen;
        this.status = status;
        checkTotal();
    }
}
