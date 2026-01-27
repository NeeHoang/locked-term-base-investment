package com.investment.lockedtembasedinvestment.wallet.domain.repository;

import com.investment.lockedtembasedinvestment.wallet.domain.aggregate.WalletAggregate;
import com.investment.lockedtembasedinvestment.wallet.domain.valueobject.WalletId;

import java.util.Optional;

public interface WalletRepository {

    Optional<WalletAggregate> findById(WalletId id);

    WalletAggregate save(WalletAggregate wallet);
}
