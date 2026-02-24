package com.investment.lockedtermbasedinvestment.saving.domain.valueobject;

import java.util.UUID;

public record SubscriptionId(UUID value) {

    public SubscriptionId {
        if (value == null)
            throw new IllegalArgumentException("SubscriptionId must be not null");
    }

    public UUID value() {
        return value;
    }

    public static SubscriptionId generate() {
        return new SubscriptionId(UUID.randomUUID());
    }
}
