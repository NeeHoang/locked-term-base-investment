package com.investment.lockedtermbasedinvestment.saving.application.service;

import com.investment.lockedtermbasedinvestment.common.enums.EarningTransaction;
import com.investment.lockedtermbasedinvestment.common.enums.EarningTxType;
import com.investment.lockedtermbasedinvestment.common.sharekernel.Money;
import com.investment.lockedtermbasedinvestment.saving.infrastructure.persistence.EarningTransactionEntity;
import com.investment.lockedtermbasedinvestment.saving.infrastructure.repository.JpaEarningTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class EarningTxStatusUpdater {

    private final JpaEarningTransactionRepository earningTxRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public EarningTransactionEntity getOrCreatePending(Long earningId,
                                                       EarningTxType type,
                                                       Money before) {
        Optional<EarningTransactionEntity> existingPending = earningTxRepository.findLatestPending(
                earningId,
                type,
                EarningTransaction.PENDING
        );

        if (existingPending.isPresent()) {
            log.info("[TX_REUSE] Found existing PENDING transaction for earning {}. Reusing...", earningId);
            return existingPending.get();
        }

        log.info("[TX_NEW] No pending transaction found for earning {}. Creating new.", earningId);
        EarningTransactionEntity newTx = EarningTransactionEntity.createPending(earningId, type, before);
        return earningTxRepository.saveAndFlush(newTx);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markSuccess(EarningTransactionEntity tx,
                            Money amount,
                            Money after) {
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
