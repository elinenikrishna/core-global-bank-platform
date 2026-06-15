package com.coreglobal.auth.application;

import com.coreglobal.common.api.CommandRequest;
import com.coreglobal.common.api.CommandResponse;
import com.coreglobal.common.event.BankingEventPublisher;
import com.coreglobal.auth.domain.model.AuthRecord;
import com.coreglobal.auth.domain.repository.AuthRepository;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthApplicationService {
    private final AuthRepository repository;
    private final BankingEventPublisher events;
    public AuthApplicationService(AuthRepository repository, BankingEventPublisher events) { this.repository = repository; this.events = events; }

    @Transactional(readOnly = true)
    public Page<AuthRecord> find(String owner, Pageable pageable) {
        return owner == null || owner.isBlank() ? repository.findAll(pageable) : repository.findByOwnerIdContainingIgnoreCase(owner, pageable);
    }

    @Transactional
    public CommandResponse execute(CommandRequest command) {
        String reference = "AUTH-" + UUID.randomUUID().toString().substring(0, 8);
        AuthRecord saved = repository.save(new AuthRecord(reference, command.ownerId() == null ? "demo-customer" : command.ownerId(), "ACCEPTED", command.amount(), String.valueOf(command.attributes())));
        UUID eventId = events.publish("notification-created", command.action(), Map.of("reference", saved.getReference(), "ownerId", saved.getOwnerId()));
        return new CommandResponse(eventId, "ACCEPTED", "notification-created", Instant.now());
    }

    public Map<String, Object> summary() {
        return Map.of("service", "auth-service", "status", "operational", "recordCount", repository.count(), "eventTopic", "notification-created", "timestamp", Instant.now());
    }
}
