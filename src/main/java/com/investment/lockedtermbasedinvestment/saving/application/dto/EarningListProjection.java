package com.investment.lockedtermbasedinvestment.saving.application.dto;

import java.math.BigDecimal;

public interface EarningListProjection {
    Long getEarningId();
    Integer getTermDays();
    BigDecimal getPrincipal();
    BigDecimal getAvailableToWithdraw();
    BigDecimal getAccruedInterest();
    Integer getHoldingDays();
    BigDecimal getProgress();
}