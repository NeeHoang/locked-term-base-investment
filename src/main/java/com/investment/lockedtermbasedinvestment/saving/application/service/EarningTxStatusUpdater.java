package com.investment.lockedtermbasedinvestment.saving.application.service;

import com.investment.lockedtermbasedinvestment.saving.infrastructure.persistence.EarningTransactionEntity;
import com.investment.lockedtermbasedinvestment.saving.infrastructure.repository.JpaEarningTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EarningTxStatusUpdater {

    private final JpaEarningTransactionRepository earningTxRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markFailed(EarningTransactionEntity tx) {
        tx.markFailed();
        earningTxRepository.saveAndFlush(tx);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markPending(EarningTransactionEntity tx) {
        // status = PENDING
        earningTxRepository.saveAndFlush(tx);
    }
}
