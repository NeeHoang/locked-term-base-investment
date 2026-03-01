package com.investment.lockedtermbasedinvestment.saving.application.service;

import com.investment.lockedtermbasedinvestment.common.enums.EarningTransaction;
import com.investment.lockedtermbasedinvestment.common.enums.EarningTxType;
import com.investment.lockedtermbasedinvestment.common.sharekernel.Money;
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
    public EarningTransactionEntity savePending(Long earningId, EarningTxType type, Money before) {
        EarningTransactionEntity tx = EarningTransactionEntity.createPending(earningId, type, before);
        return earningTxRepository.saveAndFlush(tx);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markSuccess(EarningTransactionEntity tx, Money amount, Money after) {
        earningTxRepository.updateSuccessStatus(
                tx.getTxId(),
                EarningTransaction.SUCCESS,
                amount.toBigDecimal(),
                after.toBigDecimal()
        );
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markFailed(EarningTransactionEntity tx) {
        earningTxRepository.updateStatus(tx.getTxId(), EarningTransaction.FAILED);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markPending(EarningTransactionEntity tx) {
        earningTxRepository.updateStatus(tx.getTxId(), EarningTransaction.PENDING);
    }
}
