package com.coreglobal.notification.domain.repository;

import com.coreglobal.notification.domain.model.NotificationRecord;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<NotificationRecord, UUID> {
    Page<NotificationRecord> findByOwnerIdContainingIgnoreCase(String ownerId, Pageable pageable);
}
