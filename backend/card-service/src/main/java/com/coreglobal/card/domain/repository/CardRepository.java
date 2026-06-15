package com.coreglobal.card.domain.repository;

import com.coreglobal.card.domain.model.CardRecord;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<CardRecord, UUID> {
    Page<CardRecord> findByOwnerIdContainingIgnoreCase(String ownerId, Pageable pageable);
}
