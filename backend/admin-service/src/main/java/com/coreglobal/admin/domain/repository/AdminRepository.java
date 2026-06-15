package com.coreglobal.admin.domain.repository;

import com.coreglobal.admin.domain.model.AdminRecord;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<AdminRecord, UUID> {
    Page<AdminRecord> findByOwnerIdContainingIgnoreCase(String ownerId, Pageable pageable);
}
