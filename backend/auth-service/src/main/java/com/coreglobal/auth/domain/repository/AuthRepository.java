package com.coreglobal.auth.domain.repository;

import com.coreglobal.auth.domain.model.AuthRecord;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<AuthRecord, UUID> {
    Page<AuthRecord> findByOwnerIdContainingIgnoreCase(String ownerId, Pageable pageable);
}
