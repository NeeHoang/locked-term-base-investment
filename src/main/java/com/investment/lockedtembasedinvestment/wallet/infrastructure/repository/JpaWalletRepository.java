package com.investment.lockedtembasedinvestment.wallet.infrastructure.repository;

import com.investment.lockedtembasedinvestment.wallet.infrastructure.persistence.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaWalletRepository extends JpaRepository<WalletEntity, Long> {
}
