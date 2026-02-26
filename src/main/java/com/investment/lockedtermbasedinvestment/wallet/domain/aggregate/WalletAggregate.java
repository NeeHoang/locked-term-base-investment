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

    public void depositEarnings(Money interestAmount) {
        if (interestAmount == null || interestAmount.isNegative() || interestAmount.isZero()) {
            throw new WalletException(
                    WalletErrorCode.INVALID_AMOUNT,
                    "Interest amount must be positive"
            );
        }

        this.balanceAvailable = this.balanceAvailable.add(interestAmount);
        this.totalBalance = this.totalBalance.add(interestAmount);

        validateInvariants();
    }

    public void releasePrincipalToEarning(Money originalPrincipal) {
        if (originalPrincipal == null || originalPrincipal.isNegative() || originalPrincipal.isZero()) {
            throw new WalletException(
                    WalletErrorCode.INVALID_AMOUNT,
                    "Principal must be positive");
        }

        if (!this.balanceFrozen.isGreaterThanOrEqual(originalPrincipal)) {
            throw new WalletException(
                    WalletErrorCode.INSUFFICIENT_BALANCE,
                    "Frozen balance is insufficient to release"
            );
        }

        // Trừ phần tiền gốc đang bị đóng băng
        this.balanceFrozen = this.balanceFrozen.subtract(originalPrincipal);

        // Vì tiền này đã chuyển sang Earning (không còn nằm trong cấu trúc của Wallet nữa),
        // nên totalBalance bắt buộc phải giảm theo để đảm bảo invariant.
        this.totalBalance = this.balanceAvailable.add(this.balanceFrozen);

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
