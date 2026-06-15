package com.coreglobal.card.application;

import com.coreglobal.common.api.CommandRequest;
import com.coreglobal.common.api.CommandResponse;
import com.coreglobal.common.event.BankingEventPublisher;
import com.coreglobal.card.domain.model.CardRecord;
import com.coreglobal.card.domain.repository.CardRepository;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CardApplicationService {
    private final CardRepository repository;
    private final BankingEventPublisher events;
    public CardApplicationService(CardRepository repository, BankingEventPublisher events) { this.repository = repository; this.events = events; }

    @Transactional(readOnly = true)
    public Page<CardRecord> find(String owner, Pageable pageable) {
        return owner == null || owner.isBlank() ? repository.findAll(pageable) : repository.findByOwnerIdContainingIgnoreCase(owner, pageable);
    }

    @Transactional
    public CommandResponse execute(CommandRequest command) {
        String reference = "CARD-" + UUID.randomUUID().toString().substring(0, 8);
        CardRecord saved = repository.save(new CardRecord(reference, command.ownerId() == null ? "demo-customer" : command.ownerId(), "ACCEPTED", command.amount(), String.valueOf(command.attributes())));
        UUID eventId = events.publish("card-status-updated", command.action(), Map.of("reference", saved.getReference(), "ownerId", saved.getOwnerId()));
        return new CommandResponse(eventId, "ACCEPTED", "card-status-updated", Instant.now());
    }

    public Map<String, Object> summary() {
        return Map.of("service", "card-service", "status", "operational", "recordCount", repository.count(), "eventTopic", "card-status-updated", "timestamp", Instant.now());
    }
}
