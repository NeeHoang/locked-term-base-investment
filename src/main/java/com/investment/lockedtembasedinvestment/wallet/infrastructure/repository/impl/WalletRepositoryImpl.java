package com.investment.lockedtembasedinvestment.wallet.infrastructure.repository.impl;

import com.investment.lockedtembasedinvestment.wallet.domain.aggregate.WalletAggregate;
import com.investment.lockedtembasedinvestment.wallet.domain.repository.WalletRepository;
import com.investment.lockedtembasedinvestment.wallet.domain.valueobject.WalletId;
import com.investment.lockedtembasedinvestment.wallet.infrastructure.persistence.WalletEntity;
import com.investment.lockedtembasedinvestment.wallet.infrastructure.repository.JpaWalletRepository;
import com.investment.lockedtembasedinvestment.wallet.mapper.WalletMapper;
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
        WalletEntity saved = jpaRepository.save(entity);

        if (aggregate.getId() == null) {
            aggregate.assignId(
                    new WalletId(saved.getWalletId())
            );
        }
    }
}
