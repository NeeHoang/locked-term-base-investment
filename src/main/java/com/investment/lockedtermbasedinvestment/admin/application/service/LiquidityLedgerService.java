package com.investment.lockedtermbasedinvestment.admin.application.service;

import com.investment.lockedtermbasedinvestment.admin.api.dto.response.LiquidityLedgerResponse;

import java.util.List;

public interface LiquidityLedgerService {

    List<LiquidityLedgerResponse> getAllTx();
}
