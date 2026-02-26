package com.investment.lockedtermbasedinvestment.saving.application.cron;

import com.investment.lockedtermbasedinvestment.common.enums.BatchStatus;
import com.investment.lockedtermbasedinvestment.common.enums.SubscriptionStatus;
import com.investment.lockedtermbasedinvestment.saving.api.dto.response.BatchProcessResponse;
import com.investment.lockedtermbasedinvestment.saving.api.dto.response.SkippedItemResponse;
import com.investment.lockedtermbasedinvestment.saving.domain.aggregate.SubscriptionAggregate;
import com.investment.lockedtermbasedinvestment.saving.domain.exception.DuplicateDailyInterestException;
import com.investment.lockedtermbasedinvestment.saving.domain.repository.SubscriptionRepository;
import com.investment.lockedtermbasedinvestment.saving.application.service.SubscriptionAccrualProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DailyInterestAccrualJob {

    private final SubscriptionRepository subscriptionRepository;

    private final SubscriptionAccrualProcessor accrualProcessor;

    public BatchProcessResponse accrueDailyInterestOrMature(LocalDate today) {

        List<SubscriptionAggregate> allActive =
                subscriptionRepository.findByStatus(SubscriptionStatus.ACTIVE);

        int total = allActive.size();
        int processed = 0;
        int skipped = 0;
        int ignored = 0;

        List<SkippedItemResponse> duplicates = new ArrayList<>();

        for (SubscriptionAggregate subscription : allActive) {

            if (!subscription.isEligibleForDailyAccrual(today)) {
                ignored++;
                continue;
            }

            try {
                accrualProcessor.accrueForSubscription(subscription, today);
                processed++;

            } catch (DuplicateDailyInterestException ex) {
                skipped++;
                duplicates.add(new SkippedItemResponse(
                        subscription.getId().value().toString(),
                        ex.getEarningId(),
                        "DUPLICATE_DAILY_INTEREST"
                ));

                log.info(
                        "Skip duplicate interest. subscriptionId={}, earningId={}",
                        subscription.getId(),
                        ex.getEarningId()
                );
            } catch (Exception ex) {
                skipped++;
                duplicates.add(new SkippedItemResponse(
                        subscription.getId().value().toString(),
                        null,
                        "UNEXPECTED_ERROR"
                ));

                log.error(
                        "Daily interest failed. subscriptionId={}, data={}",
                        subscription.getId(),
                        today,
                        ex
                );
            }
        }

        BatchStatus status;
        if (processed == 0 && skipped == 0) {
            status = BatchStatus.SUCCESS;
        } else if (processed + skipped == total) {
            status = BatchStatus.SUCCESS;
        } else if (processed == 0) {
            status = BatchStatus.FAILED;
        } else {
            status = BatchStatus.PARTIAL_SUCCESS;
        }

        return new BatchProcessResponse(
                status,
                today,
                new BatchProcessResponse.Summary(
                        total,
                        processed,
                        skipped,
                        ignored
                ),
                List.copyOf(duplicates)
        );
    }
}