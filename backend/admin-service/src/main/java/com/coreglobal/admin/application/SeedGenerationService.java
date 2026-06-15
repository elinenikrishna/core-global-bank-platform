package com.coreglobal.admin.application;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class SeedGenerationService {
    private static final Logger log = LoggerFactory.getLogger(SeedGenerationService.class);
    private final JdbcTemplate jdbc;
    private final ExecutorService workers = Executors.newFixedThreadPool(Math.max(2, Runtime.getRuntime().availableProcessors() / 2));
    private final Map<UUID, SeedStatus> jobs = new ConcurrentHashMap<>();

    public SeedGenerationService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
        jdbc.execute("CREATE TABLE IF NOT EXISTS CGB_SEED_ENTITY (ID VARCHAR(36) PRIMARY KEY, ENTITY_TYPE VARCHAR(32), REFERENCE VARCHAR(80), PAYLOAD VARCHAR(1000), CREATED_AT TIMESTAMP)");
        jdbc.execute("CREATE INDEX IF NOT EXISTS IDX_SEED_ENTITY_TYPE ON CGB_SEED_ENTITY(ENTITY_TYPE)");
    }

    public UUID start(SeedRequest request) {
        UUID jobId = UUID.randomUUID();
        long total = request.counts().values().stream().mapToLong(Long::longValue).sum();
        AtomicLong completed = new AtomicLong();
        jobs.put(jobId, new SeedStatus(jobId, "RUNNING", 0, total, Instant.now(), null));
        CompletableFuture.runAsync(() -> {
            request.counts().forEach((type, count) -> generateType(jobId, type, count, request.batchSize(), completed, total));
            jobs.put(jobId, new SeedStatus(jobId, "COMPLETED", completed.get(), total, jobs.get(jobId).startedAt(), Instant.now()));
        }, workers).exceptionally(error -> {
            log.error("seed_job_failed jobId={}", jobId, error);
            jobs.put(jobId, new SeedStatus(jobId, "FAILED", completed.get(), total, jobs.get(jobId).startedAt(), Instant.now()));
            return null;
        });
        return jobId;
    }

    public SeedStatus status(UUID id) { return jobs.get(id); }

    private void generateType(UUID jobId, String type, long count, int batchSize, AtomicLong completed, long total) {
        Faker faker = new Faker();
        for (long offset = 0; offset < count; offset += batchSize) {
            int size = (int) Math.min(batchSize, count - offset);
            List<Object[]> batch = new ArrayList<>(size);
            for (int index = 0; index < size; index++) {
                batch.add(new Object[]{UUID.randomUUID().toString(), type, type.toUpperCase() + "-" + (offset + index), faker.json().toString(), java.sql.Timestamp.from(Instant.now())});
            }
            jdbc.batchUpdate("INSERT INTO CGB_SEED_ENTITY (ID, ENTITY_TYPE, REFERENCE, PAYLOAD, CREATED_AT) VALUES (?, ?, ?, ?, ?)", batch);
            long done = completed.addAndGet(size);
            jobs.put(jobId, new SeedStatus(jobId, "RUNNING", done, total, jobs.get(jobId).startedAt(), null));
            log.info("seed_progress jobId={} entityType={} completed={} total={} percent={}", jobId, type, done, total, Math.round(done * 100.0 / total));
        }
    }

    public record SeedRequest(Map<String, Long> counts, int batchSize) {
        public SeedRequest {
            if (batchSize < 1 || batchSize > 10_000) batchSize = 1_000;
            if (counts == null || counts.isEmpty()) counts = demo().counts();
        }
        public static SeedRequest demo() { return new SeedRequest(Map.of("customers", 25L, "accounts", 60L, "transactions", 250L, "cards", 20L, "loans", 8L, "offers", 20L), 100); }
        public static SeedRequest portfolioScale() { return new SeedRequest(Map.of("customers", 3_000_000L, "accounts", 8_000_000L, "transactions", 25_000_000L, "cards", 1_000_000L, "loans", 500_000L, "offers", 2_000_000L), 5_000); }
    }
    public record SeedStatus(UUID jobId, String state, long completed, long total, Instant startedAt, Instant completedAt) {}
}

