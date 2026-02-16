package com.investment.lockedtermbasedinvestment.saving.api.dto.request;

import com.investment.lockedtermbasedinvestment.common.sharekernel.Money;

public record CreateSubscriptionRequest(
        Long productId,
        Money principal
) {
}
