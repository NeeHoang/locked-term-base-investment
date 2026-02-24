package com.investment.lockedtermbasedinvestment.saving.application.cron;

import com.investment.lockedtermbasedinvestment.saving.api.dto.response.BatchProcessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;

@Component
@RequiredArgsConstructor
public class DailyInterestAccrualService {

    private static final ZoneId VN_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");

    private final DailyInterestAccrualJob accrualJob;

    public BatchProcessResponse trigger(LocalDate runDate) {
        return accrualJob.accrueDailyInterest(runDate);
    }

    @Scheduled(
            cron = "0 5 0 * * *",
            zone = "Asia/Ho_Chi_Minh"
    ) // 00:05 hang ngay
    public void run() {
        LocalDate today = LocalDate.now(VN_ZONE);
        trigger(today);
    }
}
