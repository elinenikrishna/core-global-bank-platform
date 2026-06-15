package com.coreglobal.account.application;

import com.coreglobal.common.api.CommandRequest;
import com.coreglobal.common.api.CommandResponse;
import com.coreglobal.common.event.BankingEventPublisher;
import com.coreglobal.account.domain.model.AccountRecord;
import com.coreglobal.account.domain.repository.AccountRepository;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountApplicationService {
    private final AccountRepository repository;
    private final BankingEventPublisher events;
    public AccountApplicationService(AccountRepository repository, BankingEventPublisher events) { this.repository = repository; this.events = events; }

    @Transactional(readOnly = true)
    public Page<AccountRecord> find(String owner, Pageable pageable) {
        return owner == null || owner.isBlank() ? repository.findAll(pageable) : repository.findByOwnerIdContainingIgnoreCase(owner, pageable);
    }

    @Transactional
    public CommandResponse execute(CommandRequest command) {
        String reference = "ACCOUNT-" + UUID.randomUUID().toString().substring(0, 8);
        AccountRecord saved = repository.save(new AccountRecord(reference, command.ownerId() == null ? "demo-customer" : command.ownerId(), "ACCEPTED", command.amount(), String.valueOf(command.attributes())));
        UUID eventId = events.publish("account-updated", command.action(), Map.of("reference", saved.getReference(), "ownerId", saved.getOwnerId()));
        return new CommandResponse(eventId, "ACCEPTED", "account-updated", Instant.now());
    }

    public Map<String, Object> summary() {
        return Map.of("service", "account-service", "status", "operational", "recordCount", repository.count(), "eventTopic", "account-updated", "timestamp", Instant.now());
    }
}
