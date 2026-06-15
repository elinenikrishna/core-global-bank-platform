package com.coreglobal.frauddetection.application;

import com.coreglobal.common.api.CommandRequest;
import com.coreglobal.common.api.CommandResponse;
import com.coreglobal.common.event.BankingEventPublisher;
import com.coreglobal.frauddetection.domain.model.FraudAlertRecord;
import com.coreglobal.frauddetection.domain.repository.FraudAlertRepository;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FraudAlertApplicationService {
    private final FraudAlertRepository repository;
    private final BankingEventPublisher events;
    public FraudAlertApplicationService(FraudAlertRepository repository, BankingEventPublisher events) { this.repository = repository; this.events = events; }

    @Transactional(readOnly = true)
    public Page<FraudAlertRecord> find(String owner, Pageable pageable) {
        return owner == null || owner.isBlank() ? repository.findAll(pageable) : repository.findByOwnerIdContainingIgnoreCase(owner, pageable);
    }

    @Transactional
    public CommandResponse execute(CommandRequest command) {
        String reference = "FRAUDDETECTION-" + UUID.randomUUID().toString().substring(0, 8);
        FraudAlertRecord saved = repository.save(new FraudAlertRecord(reference, command.ownerId() == null ? "demo-customer" : command.ownerId(), "ACCEPTED", command.amount(), String.valueOf(command.attributes())));
        UUID eventId = events.publish("fraud-alert-created", command.action(), Map.of("reference", saved.getReference(), "ownerId", saved.getOwnerId()));
        return new CommandResponse(eventId, "ACCEPTED", "fraud-alert-created", Instant.now());
    }

    public Map<String, Object> summary() {
        return Map.of("service", "fraud-detection-service", "status", "operational", "recordCount", repository.count(), "eventTopic", "fraud-alert-created", "timestamp", Instant.now());
    }
}
