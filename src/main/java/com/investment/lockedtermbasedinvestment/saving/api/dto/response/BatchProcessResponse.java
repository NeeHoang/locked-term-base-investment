package com.investment.lockedtermbasedinvestment.saving.api.dto.response;

import com.investment.lockedtermbasedinvestment.common.enums.BatchStatus;

import java.time.LocalDate;
import java.util.List;

public record BatchProcessResponse(
        BatchStatus status,
        LocalDate date,
        Summary summary,
        List<SkippedItemResponse> skippedItems
) {

    public record Summary(
        int total,
        int processed,
        int skipped,
        int ignored
    ) {}
}
