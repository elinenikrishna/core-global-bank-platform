package com.coreglobal.transaction.application;

import com.coreglobal.common.api.CommandRequest;
import com.coreglobal.common.api.CommandResponse;
import com.coreglobal.common.event.BankingEventPublisher;
import com.coreglobal.transaction.domain.model.TransactionRecord;
import com.coreglobal.transaction.domain.repository.TransactionRepository;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionApplicationService {
    private final TransactionRepository repository;
    private final BankingEventPublisher events;
    public TransactionApplicationService(TransactionRepository repository, BankingEventPublisher events) { this.repository = repository; this.events = events; }

    @Transactional(readOnly = true)
    public Page<TransactionRecord> find(String owner, Pageable pageable) {
        return owner == null || owner.isBlank() ? repository.findAll(pageable) : repository.findByOwnerIdContainingIgnoreCase(owner, pageable);
    }

    @Transactional
    public CommandResponse execute(CommandRequest command) {
        String reference = "TRANSACTION-" + UUID.randomUUID().toString().substring(0, 8);
        TransactionRecord saved = repository.save(new TransactionRecord(reference, command.ownerId() == null ? "demo-customer" : command.ownerId(), "ACCEPTED", command.amount(), String.valueOf(command.attributes())));
        UUID eventId = events.publish("transaction-created", command.action(), Map.of("reference", saved.getReference(), "ownerId", saved.getOwnerId()));
        return new CommandResponse(eventId, "ACCEPTED", "transaction-created", Instant.now());
    }

    public Map<String, Object> summary() {
        return Map.of("service", "transaction-service", "status", "operational", "recordCount", repository.count(), "eventTopic", "transaction-created", "timestamp", Instant.now());
    }
}
