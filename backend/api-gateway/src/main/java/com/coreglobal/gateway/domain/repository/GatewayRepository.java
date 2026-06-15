package com.coreglobal.gateway.domain.repository;

import com.coreglobal.gateway.domain.model.GatewayRecord;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GatewayRepository extends JpaRepository<GatewayRecord, UUID> {
    Page<GatewayRecord> findByOwnerIdContainingIgnoreCase(String ownerId, Pageable pageable);
}
