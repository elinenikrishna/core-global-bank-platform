package com.coreglobal.frauddetection.application;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.springframework.stereotype.Service;

@Service
public class FraudRuleEngine {
    private final Executor rulePool = Executors.newFixedThreadPool(Math.max(2, Runtime.getRuntime().availableProcessors() / 2));

    public Assessment assess(TransactionSignal signal) {
        List<CompletableFuture<RuleResult>> checks = List.of(
                check("HIGH_VALUE", signal.amount().compareTo(new BigDecimal("10000")) > 0, .45),
                check("NEW_DEVICE", signal.newDevice(), .30),
                check("IMPOSSIBLE_TRAVEL", signal.minutesSinceLastCountry() < 60, .60),
                check("VELOCITY", signal.transactionsLastMinute() > 8, .55));
        List<RuleResult> triggered = checks.stream().map(CompletableFuture::join).filter(RuleResult::triggered).toList();
        double score = Math.min(1, triggered.stream().mapToDouble(RuleResult::weight).sum());
        return new Assessment(score, score >= .65 ? "REVIEW" : "ALLOW", triggered, Instant.now());
    }

    private CompletableFuture<RuleResult> check(String rule, boolean triggered, double weight) {
        return CompletableFuture.supplyAsync(() -> new RuleResult(rule, triggered, triggered ? weight : 0), rulePool);
    }

    public record TransactionSignal(BigDecimal amount, boolean newDevice, int minutesSinceLastCountry, int transactionsLastMinute) {}
    public record RuleResult(String rule, boolean triggered, double weight) {}
    public record Assessment(double score, String decision, List<RuleResult> rules, Instant assessedAt) {}
}

