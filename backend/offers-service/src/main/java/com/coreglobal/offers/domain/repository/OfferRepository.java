package com.coreglobal.offers.domain.repository;

import com.coreglobal.offers.domain.model.OfferRecord;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferRepository extends JpaRepository<OfferRecord, UUID> {
    Page<OfferRecord> findByOwnerIdContainingIgnoreCase(String ownerId, Pageable pageable);
}
