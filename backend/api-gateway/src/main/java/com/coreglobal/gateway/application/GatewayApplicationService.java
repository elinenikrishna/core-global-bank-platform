package com.coreglobal.gateway.application;

import com.coreglobal.common.api.CommandRequest;
import com.coreglobal.common.api.CommandResponse;
import com.coreglobal.common.event.BankingEventPublisher;
import com.coreglobal.gateway.domain.model.GatewayRecord;
import com.coreglobal.gateway.domain.repository.GatewayRepository;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GatewayApplicationService {
    private final GatewayRepository repository;
    private final BankingEventPublisher events;
    public GatewayApplicationService(GatewayRepository repository, BankingEventPublisher events) { this.repository = repository; this.events = events; }

    @Transactional(readOnly = true)
    public Page<GatewayRecord> find(String owner, Pageable pageable) {
        return owner == null || owner.isBlank() ? repository.findAll(pageable) : repository.findByOwnerIdContainingIgnoreCase(owner, pageable);
    }

    @Transactional
    public CommandResponse execute(CommandRequest command) {
        String reference = "GATEWAY-" + UUID.randomUUID().toString().substring(0, 8);
        GatewayRecord saved = repository.save(new GatewayRecord(reference, command.ownerId() == null ? "demo-customer" : command.ownerId(), "ACCEPTED", command.amount(), String.valueOf(command.attributes())));
        UUID eventId = events.publish("gateway-events", command.action(), Map.of("reference", saved.getReference(), "ownerId", saved.getOwnerId()));
        return new CommandResponse(eventId, "ACCEPTED", "gateway-events", Instant.now());
    }

    public Map<String, Object> summary() {
        return Map.of("service", "api-gateway", "status", "operational", "recordCount", repository.count(), "eventTopic", "gateway-events", "timestamp", Instant.now());
    }
}
