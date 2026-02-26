package com.investment.lockedtermbasedinvestment.saving.application.service.impl;

import com.investment.lockedtermbasedinvestment.saving.api.dto.response.EarningTransactionResponse;
import com.investment.lockedtermbasedinvestment.saving.api.dto.response.InterestTransactionResponse;
import com.investment.lockedtermbasedinvestment.saving.api.dto.response.WithdrawTransactionResponse;
import com.investment.lockedtermbasedinvestment.saving.application.service.TransactionService;
import com.investment.lockedtermbasedinvestment.saving.infrastructure.persistence.EarningTransactionEntity;
import com.investment.lockedtermbasedinvestment.saving.infrastructure.persistence.InterestTransactionEntity;
import com.investment.lockedtermbasedinvestment.saving.infrastructure.persistence.WithdrawTransactionEntity;
import com.investment.lockedtermbasedinvestment.saving.infrastructure.repository.JpaEarningTransactionRepository;
import com.investment.lockedtermbasedinvestment.saving.infrastructure.repository.JpaInterestTransactionRepository;
import com.investment.lockedtermbasedinvestment.saving.infrastructure.repository.JpaWithdrawTransactionRepository;
import com.investment.lockedtermbasedinvestment.wallet.domain.valueobject.WalletId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final JpaWithdrawTransactionRepository jpaWithdrawTx;
    private final JpaEarningTransactionRepository jpaEarningTx;
    private final JpaInterestTransactionRepository jpaInterestTx;

    @Override
    public List<WithdrawTransactionResponse> getWithdrawTx(String walletId) {

        UUID walletUUID = WalletId.from(walletId).value();

        return jpaWithdrawTx
                .findAllByWalletId(walletUUID)
                .stream()
                .map(this::mapWithdraw)
                .toList();
    }

    @Override
    public List<InterestTransactionResponse> getInterestTx(String walletId) {

        UUID walletUUID = WalletId.from(walletId).value();

        return jpaInterestTx
                .findAllByWalletId(walletUUID)
                .stream()
                .map(this::mapInterest)
                .toList();
    }

    @Override
    public List<EarningTransactionResponse> getEarningTx(String walletId) {

        UUID walletUUID = WalletId.from(walletId).value();

        return jpaEarningTx
                .findAllByWalletId(walletUUID)
                .stream()
                .map(this::mapEarning)
                .toList();
    }

    private WithdrawTransactionResponse mapWithdraw(WithdrawTransactionEntity e) {
        return new WithdrawTransactionResponse(
                e.getTxId(),
                e.getEarningId(),
                e.getAvailableBefore(),
                e.getAmount(),
                e.getAvailableAfter(),
                e.getCreatedAt()
        );
    }

    private InterestTransactionResponse mapInterest(InterestTransactionEntity e) {
        return new InterestTransactionResponse(
                e.getTxId(),
                e.getEarningId(),
                e.getDate(),
                e.getAmount(),
                e.getCreatedAt()
        );
    }

    private EarningTransactionResponse mapEarning(EarningTransactionEntity e) {
        return new EarningTransactionResponse(
                e.getTxId(),
                e.getEarningId(),
                e.getTxType(),
                e.getStatus(),
                e.getAvailableBefore(),
                e.getAmount(),
                e.getAvailableAfter(),
                e.getCreatedAt()
        );
    }
}
