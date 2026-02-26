package com.investment.lockedtermbasedinvestment.admin.infrastructure.persistence;

import com.investment.lockedtermbasedinvestment.common.enums.LiquidityPoolStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Getter @Setter
@Table(name = "liquidity_pool")
public class LiquidityPoolEntity {

    @Id
    @Column(name = "pool_id", columnDefinition = "BYTEA", nullable = false, updatable = false)
    private byte[] id; // ULID (16 bytes)

    @Column(name = "total_amount", precision = 18, scale = 8, nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "min_threshold", precision = 18, scale = 8, nullable = false)
    private BigDecimal minThreshold;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private LiquidityPoolStatus status;

    @Column(name = "last_injected_at", nullable = false)
    private Instant lastInjectedAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;
}
