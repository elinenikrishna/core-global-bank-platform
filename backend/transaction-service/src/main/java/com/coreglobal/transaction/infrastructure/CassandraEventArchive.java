package com.coreglobal.transaction.infrastructure;

import com.datastax.oss.driver.api.core.CqlSession;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.UUID;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("cassandra")
public class CassandraEventArchive implements AutoCloseable {
    private final CqlSession session;

    public CassandraEventArchive() {
        String host = System.getenv().getOrDefault("CASSANDRA_CONTACT_POINTS", "localhost");
        this.session = CqlSession.builder().addContactPoint(new InetSocketAddress(host, 9042))
                .withLocalDatacenter("datacenter1").withKeyspace("core_global_events").build();
    }

    public void append(String accountId, String eventType, BigDecimal amount, String payload, String correlationId) {
        Instant now = Instant.now();
        session.execute("INSERT INTO transaction_events_by_account (account_id,event_day,occurred_at,event_id,event_type,amount,payload,correlation_id) VALUES (?,?,?,?,?,?,?,?)",
                accountId, LocalDate.ofInstant(now, ZoneOffset.UTC), now, UUID.randomUUID(), eventType, amount, payload, correlationId);
    }

    @Override public void close() { session.close(); }
}

