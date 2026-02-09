package com.investment.lockedtembasedinvestment.saving.domain.valueobject;

import java.util.UUID;

public record SubscriptionId(UUID value) {

    public SubscriptionId {
        if (value == null)
            throw new IllegalArgumentException("SubscriptionId must be not null");
    }
}
