package com.investment.lockedtembasedinvestment.saving.infrastructure.persistence;

import com.investment.lockedtembasedinvestment.enums.LockedProductStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "locked_products")
@Getter @Setter
public class LockedProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "term_days", nullable = false)
    private Integer termDays;

    @Column(name = "interest_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal interestRate;

    @Column(name = "min_amount", precision = 18, scale = 8)
    private BigDecimal minAmount;

    @Column(name = "max_amount", precision = 18, scale = 8)
    private BigDecimal maxAmount;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private LockedProductStatus status; // ACTIVE, INACTIVE, FULLED

    @Column(name = "available_quota", precision = 18, scale = 8)
    private BigDecimal availableQuota;

    @Column(name = "total_quota", precision = 18, scale = 8)
    private BigDecimal totalQuota;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;
}