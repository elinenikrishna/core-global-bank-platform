package com.coreglobal.customer.application;

import com.coreglobal.common.api.CommandRequest;
import com.coreglobal.common.api.CommandResponse;
import com.coreglobal.common.event.BankingEventPublisher;
import com.coreglobal.customer.domain.model.CustomerRecord;
import com.coreglobal.customer.domain.repository.CustomerRepository;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerApplicationService {
    private final CustomerRepository repository;
    private final BankingEventPublisher events;
    public CustomerApplicationService(CustomerRepository repository, BankingEventPublisher events) { this.repository = repository; this.events = events; }

    @Transactional(readOnly = true)
    public Page<CustomerRecord> find(String owner, Pageable pageable) {
        return owner == null || owner.isBlank() ? repository.findAll(pageable) : repository.findByOwnerIdContainingIgnoreCase(owner, pageable);
    }

    @Transactional
    public CommandResponse execute(CommandRequest command) {
        String reference = "CUSTOMER-" + UUID.randomUUID().toString().substring(0, 8);
        CustomerRecord saved = repository.save(new CustomerRecord(reference, command.ownerId() == null ? "demo-customer" : command.ownerId(), "ACCEPTED", command.amount(), String.valueOf(command.attributes())));
        UUID eventId = events.publish("customer-updated", command.action(), Map.of("reference", saved.getReference(), "ownerId", saved.getOwnerId()));
        return new CommandResponse(eventId, "ACCEPTED", "customer-updated", Instant.now());
    }

    public Map<String, Object> summary() {
        return Map.of("service", "customer-service", "status", "operational", "recordCount", repository.count(), "eventTopic", "customer-updated", "timestamp", Instant.now());
    }
}
