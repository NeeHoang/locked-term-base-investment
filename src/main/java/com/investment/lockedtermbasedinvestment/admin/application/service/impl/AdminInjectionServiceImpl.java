package com.investment.lockedtermbasedinvestment.admin.application.service.impl;

import com.github.f4b6a3.ulid.Ulid;
import com.github.f4b6a3.ulid.UlidCreator;
import com.investment.lockedtermbasedinvestment.admin.api.dto.request.AdminInjectionRequest;
import com.investment.lockedtermbasedinvestment.admin.api.dto.request.InjectionRequest;
import com.investment.lockedtermbasedinvestment.admin.api.dto.response.AdminInjectionResponse;
import com.investment.lockedtermbasedinvestment.admin.application.service.AdminInjectionService;
import com.investment.lockedtermbasedinvestment.admin.domain.factory.LiquidityPoolFactory;
import com.investment.lockedtermbasedinvestment.admin.domain.valueobject.LiquidityPoolId;
import com.investment.lockedtermbasedinvestment.admin.infrastructure.persistence.AdminInjectionEntity;
import com.investment.lockedtermbasedinvestment.admin.infrastructure.persistence.LiquidityLedgerEntity;
import com.investment.lockedtermbasedinvestment.admin.infrastructure.persistence.LiquidityPoolEntity;
import com.investment.lockedtermbasedinvestment.admin.infrastructure.repository.JpaAdminInjectionRepository;
import com.investment.lockedtermbasedinvestment.admin.infrastructure.repository.JpaLiquidityLedgerRepository;
import com.investment.lockedtermbasedinvestment.admin.infrastructure.repository.JpaLiquidityPoolRepository;
import com.investment.lockedtermbasedinvestment.common.enums.LiquidityTransactionType;
import com.investment.lockedtermbasedinvestment.common.sharekernel.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AdminInjectionServiceImpl implements AdminInjectionService {

    private static final String SYSTEM_ADMIN_ULID = "01HXQZK7M9F0A8K3R5YJ2D6V4B";

    private final JpaLiquidityPoolRepository liquidityPoolRepository;
    private final JpaAdminInjectionRepository adminInjectionRepository;
    private final JpaLiquidityLedgerRepository liquidityLedgerRepository;

    private final LiquidityPoolFactory liquidityPoolFactory;

    @Override
    public AdminInjectionResponse create(AdminInjectionRequest request) {
        return null;
    }

    @Override
    @Transactional
    public AdminInjectionResponse inject(InjectionRequest request) {

        // Load single liquidity pool
        LiquidityPoolEntity poolEntity = liquidityPoolRepository.findSoloRecord();
        if (poolEntity == null) {
            throw new IllegalStateException("Liquidity pool not initialized");
        }

        Money injectAmount = Money.of(request.amount());

        // Rehydrate aggregate
        var aggregate = liquidityPoolFactory.rehydrate(
                LiquidityPoolId.of(poolEntity.getId()),
                Money.of(poolEntity.getTotalAmount()),
                Money.of(poolEntity.getMinThreshold())
        );

        // snapshot BEFORE
        Money before = aggregate.getTotalAmount();

        // Domain inject
        aggregate.inject(injectAmount);

        Money after = aggregate.getTotalAmount();

        // Update liquidity_pool
        poolEntity.setTotalAmount(after.toBigDecimal());
        poolEntity.setStatus(aggregate.getStatus());
        poolEntity.setLastInjectedAt(Instant.now());
        poolEntity.setUpdatedAt(Instant.now());

        liquidityPoolRepository.save(poolEntity);

        // Create admin_injection
        byte[] adminInjectionTxId = UlidCreator.getUlid().toBytes();

        AdminInjectionEntity adminInjection = new AdminInjectionEntity();
        adminInjection.setTxId(adminInjectionTxId);
        adminInjection.setAmount(injectAmount.toBigDecimal());
        adminInjection.setAdminId(
                Ulid.from(SYSTEM_ADMIN_ULID).toBytes()
        );
        adminInjection.setNote(request.note());
        adminInjection.setCreatedAt(Instant.now());

        adminInjectionRepository.save(adminInjection);

        // Create liquidity_ledger
        LiquidityLedgerEntity ledger = new LiquidityLedgerEntity();
        ledger.setTxId(UlidCreator.getUlid().toBytes());
        ledger.setTxType(LiquidityTransactionType.INJECTION);
        ledger.setLiquidityBefore(before.toBigDecimal());
        ledger.setAmount(injectAmount.toBigDecimal());
        ledger.setLiquidityAfter(after.toBigDecimal());
        ledger.setReferenceId(adminInjectionTxId);

        liquidityLedgerRepository.save(ledger);

        // Response
        return AdminInjectionResponse.builder()
                .txId(adminInjectionTxId)
                .amount(injectAmount.toBigDecimal())
                .liquidityBefore(before.toBigDecimal())
                .liquidityAfter(after.toBigDecimal())
                .note(request.note())
                .createdAt(adminInjection.getCreatedAt())
                .build();
    }
}

