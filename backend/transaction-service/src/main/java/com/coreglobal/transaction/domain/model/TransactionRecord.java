package com.coreglobal.transaction.domain.model;

import com.coreglobal.common.domain.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "CGB_transactions", indexes = {
    @Index(name = "IDX_TRANSACTION_OWNER", columnList = "ownerId"),
    @Index(name = "IDX_TRANSACTION_STATUS", columnList = "status")
})
public class TransactionRecord extends AuditableEntity {
    @Column(nullable = false, unique = true) private String reference;
    @Column(nullable = false) private String ownerId;
    @Column(nullable = false) private String status;
    @Column(precision = 19, scale = 4) private BigDecimal amount;
    @Column(length = 2000) private String details;

    protected TransactionRecord() {}
    public TransactionRecord(String reference, String ownerId, String status, BigDecimal amount, String details) {
        this.reference = reference; this.ownerId = ownerId; this.status = status; this.amount = amount; this.details = details;
    }
    public String getReference() { return reference; }
    public String getOwnerId() { return ownerId; }
    public String getStatus() { return status; }
    public BigDecimal getAmount() { return amount; }
    public String getDetails() { return details; }
    public void updateStatus(String status) { this.status = status; }
}
