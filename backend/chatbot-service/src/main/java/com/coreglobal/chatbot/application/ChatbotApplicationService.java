package com.coreglobal.chatbot.application;

import com.coreglobal.common.api.CommandRequest;
import com.coreglobal.common.api.CommandResponse;
import com.coreglobal.common.event.BankingEventPublisher;
import com.coreglobal.chatbot.domain.model.ChatbotRecord;
import com.coreglobal.chatbot.domain.repository.ChatbotRepository;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChatbotApplicationService {
    private final ChatbotRepository repository;
    private final BankingEventPublisher events;
    public ChatbotApplicationService(ChatbotRepository repository, BankingEventPublisher events) { this.repository = repository; this.events = events; }

    @Transactional(readOnly = true)
    public Page<ChatbotRecord> find(String owner, Pageable pageable) {
        return owner == null || owner.isBlank() ? repository.findAll(pageable) : repository.findByOwnerIdContainingIgnoreCase(owner, pageable);
    }

    @Transactional
    public CommandResponse execute(CommandRequest command) {
        String reference = "CHATBOT-" + UUID.randomUUID().toString().substring(0, 8);
        ChatbotRecord saved = repository.save(new ChatbotRecord(reference, command.ownerId() == null ? "demo-customer" : command.ownerId(), "ACCEPTED", command.amount(), String.valueOf(command.attributes())));
        UUID eventId = events.publish("chatbot-message-created", command.action(), Map.of("reference", saved.getReference(), "ownerId", saved.getOwnerId()));
        return new CommandResponse(eventId, "ACCEPTED", "chatbot-message-created", Instant.now());
    }

    public Map<String, Object> summary() {
        return Map.of("service", "chatbot-service", "status", "operational", "recordCount", repository.count(), "eventTopic", "chatbot-message-created", "timestamp", Instant.now());
    }
}
