package com.coreglobal.transaction.application;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ConcurrentSettlementProcessor {
    private static final Logger log = LoggerFactory.getLogger(ConcurrentSettlementProcessor.class);
    private final Executor settlementPool = Executors.newFixedThreadPool(Math.max(4, Runtime.getRuntime().availableProcessors()));
    private final AtomicLong settled = new AtomicLong();

    public BatchResult settle(List<String> transactionReferences) {
        List<CompletableFuture<Void>> tasks = transactionReferences.stream()
                .map(reference -> CompletableFuture.runAsync(() -> { settled.incrementAndGet(); log.info("settlement_completed reference={}", reference); }, settlementPool))
                .toList();
        CompletableFuture.allOf(tasks.toArray(CompletableFuture[]::new)).join();
        return new BatchResult(transactionReferences.size(), settled.get(), Instant.now());
    }
    public record BatchResult(int batchSize, long lifetimeSettled, Instant completedAt) {}
}

