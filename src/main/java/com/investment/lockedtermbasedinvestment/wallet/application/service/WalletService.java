package com.investment.lockedtermbasedinvestment.wallet.application.service;

import com.investment.lockedtermbasedinvestment.wallet.api.dto.request.UserWalletRequest;
import com.investment.lockedtermbasedinvestment.wallet.domain.aggregate.WalletAggregate;

public interface WalletService {

    WalletAggregate save(UserWalletRequest request);

    WalletAggregate getById(String id);

    void deleteWallet(String walletId);
}
