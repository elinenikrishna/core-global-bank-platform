package com.coreglobal.transaction.domain.repository;

import com.coreglobal.transaction.domain.model.TransactionRecord;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionRecord, UUID> {
    Page<TransactionRecord> findByOwnerIdContainingIgnoreCase(String ownerId, Pageable pageable);
}
