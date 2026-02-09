package com.investment.lockedtembasedinvestment.admin.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "admin_injection")
@Getter
@Setter
public class AdminInjectionEntity {

    @Id
    @Column(name = "tx_id", columnDefinition = "BYTEA", nullable = false, updatable = false)
    private byte[] txId; // ULID (16 bytes)

    @Column(name = "amount", precision = 18, scale = 8, nullable = false)
    private BigDecimal amount;

    @Column(name = "admin_id", columnDefinition = "BYTEA", nullable = false)
    private byte[] adminId;

    @Column(name = "note", columnDefinition = "TEXT", nullable = false)
    private String note;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void prePersist() {
        this.createdAt = Instant.now();
    }
}

