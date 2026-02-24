package com.investment.lockedtermbasedinvestment.wallet.domain.repository;

import com.investment.lockedtermbasedinvestment.wallet.domain.aggregate.WalletAggregate;
import com.investment.lockedtermbasedinvestment.wallet.domain.valueobject.WalletId;

import java.util.Optional;

public interface WalletRepository {

    Optional<WalletAggregate> findById(WalletId id);

    void save(WalletAggregate wallet);

    void delete(WalletAggregate wallet);

    void update(WalletAggregate wallet);
}
