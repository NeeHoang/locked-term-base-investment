package com.investment.lockedtermbasedinvestment.saving.application.service;

import com.investment.lockedtermbasedinvestment.saving.api.dto.response.EarningTransactionResponse;
import com.investment.lockedtermbasedinvestment.saving.api.dto.response.InterestTransactionResponse;
import com.investment.lockedtermbasedinvestment.saving.api.dto.response.WithdrawTransactionResponse;

import java.util.List;

public interface TransactionService {

    List<WithdrawTransactionResponse> getWithdrawTx(String walletId);

    List<InterestTransactionResponse> getInterestTx(String walletId);

    List<EarningTransactionResponse> getEarningTx(String walletId);
}
