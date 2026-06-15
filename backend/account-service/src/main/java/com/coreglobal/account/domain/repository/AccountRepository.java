package com.coreglobal.account.domain.repository;

import com.coreglobal.account.domain.model.AccountRecord;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountRecord, UUID> {
    Page<AccountRecord> findByOwnerIdContainingIgnoreCase(String ownerId, Pageable pageable);
}
