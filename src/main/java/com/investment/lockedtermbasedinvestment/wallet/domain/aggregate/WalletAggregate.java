package com.investment.lockedtermbasedinvestment.wallet.domain.aggregate;

import com.investment.lockedtermbasedinvestment.common.sharekernel.Money;
import com.investment.lockedtermbasedinvestment.common.enums.WalletStatus;
import com.investment.lockedtermbasedinvestment.wallet.domain.exception.WalletErrorCode;
import com.investment.lockedtermbasedinvestment.wallet.domain.exception.WalletException;
import com.investment.lockedtermbasedinvestment.wallet.domain.valueobject.WalletId;
import lombok.Getter;

@Getter
public class WalletAggregate {

    private final WalletId id;
    private Money totalBalance;
    private Money balanceAvailable;
    private Money balanceFrozen;
    private WalletStatus status;

    public WalletAggregate(
            WalletId id,
            Money totalBalance,
            Money balanceAvailable,
            Money balanceFrozen,
            WalletStatus status
    ) {
        if (id == null) {
            throw new WalletException(
                    WalletErrorCode.WALLET_ID_REQUIRED,
                    "WalletId cannot be null");
        }
        this.id = id;
        this.totalBalance = totalBalance;
        this.balanceAvailable = balanceAvailable;
        this.balanceFrozen = balanceFrozen;
        this.status = status;
        validateInvariants();
    }

    public void lockSaving(Money amount) {
        validateLockable(amount);

        this.balanceAvailable = this.balanceAvailable.subtract(amount);
        this.balanceFrozen = this.balanceFrozen.add(amount);

        validateInvariants();
    }

    public void releaseFromEarning(Money amount) {
        if (amount == null || amount.isNegative() || amount.isZero()) {
            throw new WalletException(
                    WalletErrorCode.INVALID_AMOUNT,
                    "Amount must be positive"
            );
        }

        if (!balanceFrozen.isGreaterThanOrEqual(amount)) {
            throw new WalletException(
                    WalletErrorCode.INSUFFICIENT_BALANCE,
                    "Frozen balance is insufficient to release"
            );
        }

        this.balanceFrozen = balanceFrozen.subtract(amount);
        this.balanceAvailable = balanceAvailable.add(amount);

        validateInvariants();
    }

    private void validateLockable(Money amount) {
        if (status != WalletStatus.ACTIVE) {
            throw new WalletException(
                    WalletErrorCode.WALLET_INACTIVE,
                    "Wallet is not active"
            );
        }

        if (amount == null || amount.isNegative() || amount.isZero()) {
            throw new WalletException(
                    WalletErrorCode.INVALID_AMOUNT,
                    "Amount must be greater than zero"
            );
        }

        if (!balanceAvailable.isGreaterThanOrEqual(amount)) {
            throw new WalletException(
                    WalletErrorCode.INSUFFICIENT_BALANCE,
                    "Available balance is insufficient"
            );
        }
    }

    private void validateInvariants() {
        if (!totalBalance.equals(balanceAvailable.add(balanceFrozen))) {
            throw new WalletException(
                    WalletErrorCode.TOTAL_BALANCE_MISMATCH,
                    "Total balance mismatch"
            );
        }
    }
}
