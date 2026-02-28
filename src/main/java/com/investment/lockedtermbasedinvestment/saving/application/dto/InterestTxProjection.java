package com.investment.lockedtermbasedinvestment.saving.application.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public interface InterestTxProjection {
    byte[] getTxId();
    Long getEarningId();
    UUID getSubscriptionId();
    LocalDate getDate();
    BigDecimal getAmount();
    Instant getCreatedAt();
}
