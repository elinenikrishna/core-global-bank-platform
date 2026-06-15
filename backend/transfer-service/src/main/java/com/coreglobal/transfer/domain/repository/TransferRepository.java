package com.coreglobal.transfer.domain.repository;

import com.coreglobal.transfer.domain.model.TransferRecord;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferRepository extends JpaRepository<TransferRecord, UUID> {
    Page<TransferRecord> findByOwnerIdContainingIgnoreCase(String ownerId, Pageable pageable);
}
