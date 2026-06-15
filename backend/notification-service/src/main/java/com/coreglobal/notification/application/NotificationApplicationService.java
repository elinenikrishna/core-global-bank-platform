package com.coreglobal.notification.application;

import com.coreglobal.common.api.CommandRequest;
import com.coreglobal.common.api.CommandResponse;
import com.coreglobal.common.event.BankingEventPublisher;
import com.coreglobal.notification.domain.model.NotificationRecord;
import com.coreglobal.notification.domain.repository.NotificationRepository;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationApplicationService {
    private final NotificationRepository repository;
    private final BankingEventPublisher events;
    public NotificationApplicationService(NotificationRepository repository, BankingEventPublisher events) { this.repository = repository; this.events = events; }

    @Transactional(readOnly = true)
    public Page<NotificationRecord> find(String owner, Pageable pageable) {
        return owner == null || owner.isBlank() ? repository.findAll(pageable) : repository.findByOwnerIdContainingIgnoreCase(owner, pageable);
    }

    @Transactional
    public CommandResponse execute(CommandRequest command) {
        String reference = "NOTIFICATION-" + UUID.randomUUID().toString().substring(0, 8);
        NotificationRecord saved = repository.save(new NotificationRecord(reference, command.ownerId() == null ? "demo-customer" : command.ownerId(), "ACCEPTED", command.amount(), String.valueOf(command.attributes())));
        UUID eventId = events.publish("notification-created", command.action(), Map.of("reference", saved.getReference(), "ownerId", saved.getOwnerId()));
        return new CommandResponse(eventId, "ACCEPTED", "notification-created", Instant.now());
    }

    public Map<String, Object> summary() {
        return Map.of("service", "notification-service", "status", "operational", "recordCount", repository.count(), "eventTopic", "notification-created", "timestamp", Instant.now());
    }
}
