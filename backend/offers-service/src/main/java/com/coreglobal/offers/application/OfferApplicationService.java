package com.coreglobal.offers.application;

import com.coreglobal.common.api.CommandRequest;
import com.coreglobal.common.api.CommandResponse;
import com.coreglobal.common.event.BankingEventPublisher;
import com.coreglobal.offers.domain.model.OfferRecord;
import com.coreglobal.offers.domain.repository.OfferRepository;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OfferApplicationService {
    private final OfferRepository repository;
    private final BankingEventPublisher events;
    public OfferApplicationService(OfferRepository repository, BankingEventPublisher events) { this.repository = repository; this.events = events; }

    @Transactional(readOnly = true)
    public Page<OfferRecord> find(String owner, Pageable pageable) {
        return owner == null || owner.isBlank() ? repository.findAll(pageable) : repository.findByOwnerIdContainingIgnoreCase(owner, pageable);
    }

    @Transactional
    public CommandResponse execute(CommandRequest command) {
        String reference = "OFFERS-" + UUID.randomUUID().toString().substring(0, 8);
        OfferRecord saved = repository.save(new OfferRecord(reference, command.ownerId() == null ? "demo-customer" : command.ownerId(), "ACCEPTED", command.amount(), String.valueOf(command.attributes())));
        UUID eventId = events.publish("offer-activated", command.action(), Map.of("reference", saved.getReference(), "ownerId", saved.getOwnerId()));
        return new CommandResponse(eventId, "ACCEPTED", "offer-activated", Instant.now());
    }

    public Map<String, Object> summary() {
        return Map.of("service", "offers-service", "status", "operational", "recordCount", repository.count(), "eventTopic", "offer-activated", "timestamp", Instant.now());
    }
}
