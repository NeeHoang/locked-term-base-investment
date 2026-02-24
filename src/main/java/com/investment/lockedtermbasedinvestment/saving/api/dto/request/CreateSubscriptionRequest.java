package com.investment.lockedtermbasedinvestment.saving.api.dto.request;

import com.investment.lockedtermbasedinvestment.common.sharekernel.Money;
import lombok.NonNull;

public record CreateSubscriptionRequest(

        @NonNull
        Long productId,

        @NonNull
        Money principal
) {
}
