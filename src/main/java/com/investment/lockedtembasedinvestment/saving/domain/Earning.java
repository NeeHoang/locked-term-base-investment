package com.investment.lockedtembasedinvestment.saving.domain;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
public class Earning {

    private final Long id;
    private final Long subscriptionId;

    private final BigDecimal principal;
    private BigDecimal available;
    private BigDecimal totalInterest;
    private BigDecimal penaltyAmount;
    private BigDecimal progress;
    private BigDecimal penaltyRate;
    private final BigDecimal interestPerDay;

    private int holdingDay;
    private final int termDay;

    private Instant createdAt;
    private Instant updatedAt;

    // Map 1-1 subscription, generate 1 Earning
    public Earning(Long subscriptionId, int termDay, Instant createdAt,
                   BigDecimal principal, BigDecimal interestPerDay) {
        this.id = null;
        this.subscriptionId = subscriptionId;
        this.termDay = termDay;
        this.createdAt = Instant.now();
        this.principal = principal;
        this.interestPerDay = interestPerDay;

        this.available = BigDecimal.ZERO;
        this.totalInterest = BigDecimal.ZERO;
        this.penaltyAmount = BigDecimal.ZERO;
        this.progress = BigDecimal.ZERO;
        this.penaltyRate = BigDecimal.ZERO;

        this.holdingDay = 0;

        this.updatedAt = Instant.now();
    }

    // Reconstitute DataBase
    public Earning(Long id, Long subscriptionId, int termDay, BigDecimal principal,
                   BigDecimal available, BigDecimal totalInterest, BigDecimal penaltyAmount,
                   BigDecimal progress, BigDecimal penaltyRate, BigDecimal interestPerDay,
                   int holdingDay, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.subscriptionId = subscriptionId;
        this.termDay = termDay;
        this.principal = principal;
        this.available = available;
        this.totalInterest = totalInterest;
        this.penaltyAmount = penaltyAmount;
        this.progress = progress;
        this.penaltyRate = penaltyRate;
        this.interestPerDay = interestPerDay;
        this.holdingDay = holdingDay;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Domain behaviors

    public void accrueDailyInterest() {
        if (this.interestPerDay.compareTo(BigDecimal.ZERO) <= 0) return;

        this.totalInterest = this.totalInterest.add(this.interestPerDay);
        this.available = this.available.add(this.interestPerDay);
    }
}
