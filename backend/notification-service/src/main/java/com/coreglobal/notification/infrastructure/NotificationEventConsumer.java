package com.coreglobal.notification.infrastructure;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "core.kafka.enabled", havingValue = "true")
public class NotificationEventConsumer {
    private static final Logger log = LoggerFactory.getLogger(NotificationEventConsumer.class);
    private final AtomicLong consumed = new AtomicLong();

    @KafkaListener(topics = {"transaction-created", "transfer-requested", "fraud-alert-created"}, groupId = "core-notification-service")
    public void consume(Map<String, Object> event) {
        log.info("notification_event_consumed count={} eventType={}", consumed.incrementAndGet(), event.get("eventType"));
    }
}

