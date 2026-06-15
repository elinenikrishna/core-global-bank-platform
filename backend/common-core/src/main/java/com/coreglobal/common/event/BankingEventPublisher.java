package com.coreglobal.common.event;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class BankingEventPublisher {
    private static final Logger log = LoggerFactory.getLogger(BankingEventPublisher.class);
    private final KafkaTemplate<String, Object> kafka;
    private final boolean enabled;

    public BankingEventPublisher(KafkaTemplate<String, Object> kafka, @Value("${core.kafka.enabled:false}") boolean enabled) {
        this.kafka = kafka;
        this.enabled = enabled;
    }

    public UUID publish(String topic, String eventType, Map<String, ?> payload) {
        UUID id = UUID.randomUUID();
        Map<String, Object> event = Map.of("eventId", id, "eventType", eventType, "occurredAt", Instant.now(), "correlationId", String.valueOf(MDC.get("correlationId")), "payload", payload);
        log.info("event_published topic={} eventType={} eventId={} correlationId={}", topic, eventType, id, MDC.get("correlationId"));
        if (enabled) kafka.send(topic, id.toString(), event);
        return id;
    }
}

