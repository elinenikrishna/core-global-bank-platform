package com.coreglobal.transfer.application;

import com.coreglobal.common.api.CommandRequest;
import com.coreglobal.common.api.CommandResponse;
import com.coreglobal.common.event.BankingEventPublisher;
import com.coreglobal.transfer.domain.model.TransferRecord;
import com.coreglobal.transfer.domain.repository.TransferRepository;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransferApplicationService {
    private final TransferRepository repository;
    private final BankingEventPublisher events;
    public TransferApplicationService(TransferRepository repository, BankingEventPublisher events) { this.repository = repository; this.events = events; }

    @Transactional(readOnly = true)
    public Page<TransferRecord> find(String owner, Pageable pageable) {
        return owner == null || owner.isBlank() ? repository.findAll(pageable) : repository.findByOwnerIdContainingIgnoreCase(owner, pageable);
    }

    @Transactional
    public CommandResponse execute(CommandRequest command) {
        String reference = "TRANSFER-" + UUID.randomUUID().toString().substring(0, 8);
        TransferRecord saved = repository.save(new TransferRecord(reference, command.ownerId() == null ? "demo-customer" : command.ownerId(), "ACCEPTED", command.amount(), String.valueOf(command.attributes())));
        UUID eventId = events.publish("transfer-requested", command.action(), Map.of("reference", saved.getReference(), "ownerId", saved.getOwnerId()));
        return new CommandResponse(eventId, "ACCEPTED", "transfer-requested", Instant.now());
    }

    public Map<String, Object> summary() {
        return Map.of("service", "transfer-service", "status", "operational", "recordCount", repository.count(), "eventTopic", "transfer-requested", "timestamp", Instant.now());
    }
}
