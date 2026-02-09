package com.investment.lockedtembasedinvestment.wallet.domain.aggregate;

import com.investment.lockedtembasedinvestment.common.sharekernel.Money;
import com.investment.lockedtembasedinvestment.common.enums.WalletStatus;
import com.investment.lockedtembasedinvestment.wallet.domain.exception.WalletErrorCode;
import com.investment.lockedtembasedinvestment.wallet.domain.exception.WalletException;
import com.investment.lockedtembasedinvestment.wallet.domain.valueobject.WalletId;
import lombok.Getter;

@Getter
public class WalletAggregate {

    private WalletId id;
    private Money totalBalance;
    private Money balanceAvailable;
    private Money balanceFrozen;

    private WalletStatus status;


    public void assignId(WalletId id) {
        if (this.id != null) {
            throw new IllegalStateException("WalletId already assigned"); }
        this.id = id;
    }

    private void checkTotal() {
        if (!totalBalance.equals(balanceAvailable.add(balanceFrozen))) {
            throw new WalletException(
                    "Total balance are not equal to the sum of available balance and frozen balance",
                    WalletErrorCode.TOTAL_BALANCE_MISMATCH
            );
        }
    }

    public void lockSaving(Money amount) {
        if (!balanceAvailable.isGreaterThanOrEqual(amount)) {
            throw new WalletException("The available balance is insufficient to lock.",
                    WalletErrorCode.INVALID_AMOUNT
            );
        }
        this.balanceAvailable = this.balanceAvailable.subtract(amount);
        this.balanceFrozen = this.balanceFrozen.add(amount);
        checkTotal();
    }

    public void releaseFromEarning(Money amount) {
        if (amount == null)
            throw new WalletException("Amount cannot be null",
                    WalletErrorCode.INVALID_AMOUNT);

        if (!balanceFrozen.isGreaterThanOrEqual(amount)) {
            throw new WalletException(
                    "Frozen balance is insufficient to release",
                    WalletErrorCode.INSUFFICIENT_BALANCE
            );
        }

        this.balanceFrozen = balanceFrozen.subtract(amount);
        this.balanceAvailable = balanceAvailable.add(amount);
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
