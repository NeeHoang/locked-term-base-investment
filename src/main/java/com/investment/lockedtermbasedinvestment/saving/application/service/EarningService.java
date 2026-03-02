package com.investment.lockedtermbasedinvestment.saving.application.service;

import com.investment.lockedtermbasedinvestment.saving.api.dto.request.WithdrawRequest;
import com.investment.lockedtermbasedinvestment.saving.api.dto.response.*;

import java.util.List;

public interface EarningService {

    EarningSummaryResponse getEarningSummary(String walletId);

    WithdrawResponse withdraw(WithdrawRequest request, String walletId);

    void earlyRedeem(Long earningId, String walletId);

    EarlyRedeemPreviewResponse previewEarlyRedeem(Long earningId, String walletId);

    List<EarningResponse> getEarnings(String walletId);

    SumInterestPerDay getEtsDailyInterest();

}
