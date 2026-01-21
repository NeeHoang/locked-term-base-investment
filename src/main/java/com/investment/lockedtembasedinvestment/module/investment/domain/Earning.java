package com.investment.lockedtembasedinvestment.module.investment.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Earning {

    private final Long id;
    private final Long subscriptionId;

    private BigDecimal principal;
    private BigDecimal available;
    private BigDecimal totalInterest;
    private BigDecimal penaltyAmount;
    private BigDecimal progress;
    private BigDecimal penaltyRate;
    private BigDecimal interestPerDay;

    private int holdingDay;
    private final int termDay;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Map 1-1 subscription, generate 1 Earning
    public Earning(Long subscriptionId, int termDay, LocalDateTime createdAt,
                   BigDecimal principal, BigDecimal interestPerDay) {
        this.id = null;
        this.subscriptionId = subscriptionId;
        this.termDay = termDay;
        this.createdAt = LocalDateTime.now();
        this.principal = principal;
        this.interestPerDay = interestPerDay;

        this.available = BigDecimal.ZERO;
        this.totalInterest = BigDecimal.ZERO;
        this.penaltyAmount = BigDecimal.ZERO;
        this.progress = BigDecimal.ZERO;
        this.penaltyRate = BigDecimal.ZERO;

        this.holdingDay = 0;

        this.updatedAt = LocalDateTime.now();
    }

    // Reconstitute DataBase
    public Earning(Long id, Long subscriptionId, int termDay, BigDecimal principal,
                   BigDecimal available, BigDecimal totalInterest, BigDecimal penaltyAmount,
                   BigDecimal progress, BigDecimal penaltyRate, BigDecimal interestPerDay,
                   int holdingDay, LocalDateTime createdAt, LocalDateTime updatedAt) {
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

    public void accrueDailyInterest(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) return;

        this.totalInterest = this.totalInterest.add(amount);

    }
}
