package com.investment.lockedtermbasedinvestment.wallet.infrastructure.repository.impl;

import com.investment.lockedtermbasedinvestment.wallet.domain.aggregate.WalletAggregate;
import com.investment.lockedtermbasedinvestment.wallet.domain.repository.WalletRepository;
import com.investment.lockedtermbasedinvestment.wallet.domain.valueobject.WalletId;
import com.investment.lockedtermbasedinvestment.wallet.infrastructure.persistence.WalletEntity;
import com.investment.lockedtermbasedinvestment.wallet.infrastructure.repository.JpaWalletRepository;
import com.investment.lockedtermbasedinvestment.wallet.mapper.WalletMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class WalletRepositoryImpl implements WalletRepository {

    private final JpaWalletRepository jpaRepository;

    public WalletRepositoryImpl(JpaWalletRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<WalletAggregate> findById(WalletId id) {
        return jpaRepository.findById(id.value())
                .map(WalletMapper::toDomain);
    }

    @Override
    public void save(WalletAggregate aggregate) {

        WalletEntity entity = WalletMapper.toEntity(aggregate);
        jpaRepository.save(entity);
    }

    @Override
    public void delete(WalletAggregate wallet) {
        jpaRepository.delete(WalletMapper.toEntity(wallet));
    }
}
