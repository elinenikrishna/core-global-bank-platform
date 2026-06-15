package com.coreglobal.customer.domain.repository;

import com.coreglobal.customer.domain.model.CustomerRecord;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<CustomerRecord, UUID> {
    Page<CustomerRecord> findByOwnerIdContainingIgnoreCase(String ownerId, Pageable pageable);
}
