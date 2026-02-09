package com.investment.lockedtembasedinvestment.wallet.infrastructure.repository;

import com.investment.lockedtembasedinvestment.wallet.infrastructure.persistence.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaWalletRepository extends JpaRepository<WalletEntity, UUID> {
}
