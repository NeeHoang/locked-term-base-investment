package com.investment.lockedtermbasedinvestment.saving.application.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
public interface WithdrawTxProjection {
    byte[] getTxId();
    Long getEarningId();
    LocalDate getDate();
    BigDecimal getAvailableBefore();
    BigDecimal getAvailableAfter();
    BigDecimal getAmount();
    Instant getCreatedAt();
    UUID getSubscriptionId();
}
