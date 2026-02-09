package com.investment.lockedtembasedinvestment.wallet.application.service;

import com.investment.lockedtembasedinvestment.wallet.api.dto.request.UserWalletRequest;
import com.investment.lockedtembasedinvestment.wallet.domain.aggregate.WalletAggregate;

public interface WalletService {

    WalletAggregate save(UserWalletRequest request);
}
