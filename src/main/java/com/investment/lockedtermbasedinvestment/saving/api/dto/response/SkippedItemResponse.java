package com.investment.lockedtermbasedinvestment.saving.api.dto.response;

public record SkippedItemResponse(
        String subscriptionId,
        Long earningId,
        String reason
) {}
