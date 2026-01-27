package com.investment.lockedtembasedinvestment.wallet.mapper;

import com.investment.lockedtembasedinvestment.common.Money;
import com.investment.lockedtembasedinvestment.wallet.domain.aggregate.WalletAggregate;
import com.investment.lockedtembasedinvestment.wallet.domain.valueobject.WalletId;
import com.investment.lockedtembasedinvestment.wallet.infrastructure.persistence.WalletEntity;

public class WalletMapper {

    public static WalletAggregate toDomain(WalletEntity entity) {
        return new WalletAggregate(
                new WalletId(entity.getWalletId()),
                Money.of(entity.getTotalBalance()),
                Money.of(entity.getBalanceAvailable()),
                Money.of(entity.getBalanceFrozen()),
                entity.getStatus()
        );
    }

    public static WalletEntity toEntity(WalletAggregate wallet) {
        WalletEntity entity = new WalletEntity();
        entity.setWalletId(wallet.getId().getValue()); // null khi insert
        entity.setTotalBalance(wallet.getTotalBalance().toBigDecimal());
        entity.setBalanceAvailable(wallet.getBalanceAvailable().toBigDecimal());
        entity.setBalanceFrozen(wallet.getBalanceFrozen().toBigDecimal());
        entity.setStatus(wallet.getStatus());
        return entity;
    }

}

