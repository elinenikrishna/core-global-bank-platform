package com.coreglobal.frauddetection.domain.repository;

import com.coreglobal.frauddetection.domain.model.FraudAlertRecord;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FraudAlertRepository extends JpaRepository<FraudAlertRecord, UUID> {
    Page<FraudAlertRecord> findByOwnerIdContainingIgnoreCase(String ownerId, Pageable pageable);
}
