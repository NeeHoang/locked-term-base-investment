package com.investment.lockedtermbasedinvestment.admin.application.service.impl;

import com.investment.lockedtermbasedinvestment.admin.api.dto.response.LiquidityLedgerResponse;
import com.investment.lockedtermbasedinvestment.admin.application.service.LiquidityLedgerService;
import com.investment.lockedtermbasedinvestment.admin.infrastructure.repository.JpaLiquidityLedgerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LiquidityLedgerServiceImpl implements LiquidityLedgerService {

    private final JpaLiquidityLedgerRepository jpaLiquidityLedgerRepository;

    @Override
    public List<LiquidityLedgerResponse> getAllTx() {
        return jpaLiquidityLedgerRepository.findAllOrderByCreatedAtDesc();
    }
}