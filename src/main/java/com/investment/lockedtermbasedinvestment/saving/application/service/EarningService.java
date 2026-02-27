package com.investment.lockedtermbasedinvestment.saving.application.service;

import com.investment.lockedtermbasedinvestment.saving.api.dto.request.WithdrawRequest;
import com.investment.lockedtermbasedinvestment.saving.api.dto.response.EarningSummaryResponse;
import com.investment.lockedtermbasedinvestment.saving.api.dto.response.WithdrawResponse;

public interface EarningService {

    EarningSummaryResponse getEarningSummary(String walletId);

    WithdrawResponse withdraw(WithdrawRequest request, String walletId);

    void earlyRedeem(Long earningId, String walletId);
}
