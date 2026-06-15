package com.coreglobal.admin.application;

import com.coreglobal.common.api.CommandRequest;
import com.coreglobal.common.api.CommandResponse;
import com.coreglobal.common.event.BankingEventPublisher;
import com.coreglobal.admin.domain.model.AdminRecord;
import com.coreglobal.admin.domain.repository.AdminRepository;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminApplicationService {
    private final AdminRepository repository;
    private final BankingEventPublisher events;
    public AdminApplicationService(AdminRepository repository, BankingEventPublisher events) { this.repository = repository; this.events = events; }

    @Transactional(readOnly = true)
    public Page<AdminRecord> find(String owner, Pageable pageable) {
        return owner == null || owner.isBlank() ? repository.findAll(pageable) : repository.findByOwnerIdContainingIgnoreCase(owner, pageable);
    }

    @Transactional
    public CommandResponse execute(CommandRequest command) {
        String reference = "ADMIN-" + UUID.randomUUID().toString().substring(0, 8);
        AdminRecord saved = repository.save(new AdminRecord(reference, command.ownerId() == null ? "demo-customer" : command.ownerId(), "ACCEPTED", command.amount(), String.valueOf(command.attributes())));
        UUID eventId = events.publish("admin-audit-created", command.action(), Map.of("reference", saved.getReference(), "ownerId", saved.getOwnerId()));
        return new CommandResponse(eventId, "ACCEPTED", "admin-audit-created", Instant.now());
    }

    public Map<String, Object> summary() {
        return Map.of("service", "admin-service", "status", "operational", "recordCount", repository.count(), "eventTopic", "admin-audit-created", "timestamp", Instant.now());
    }
}
