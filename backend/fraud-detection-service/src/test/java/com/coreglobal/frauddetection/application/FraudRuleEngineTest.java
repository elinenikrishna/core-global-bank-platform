package com.coreglobal.frauddetection.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class FraudRuleEngineTest {
    private final FraudRuleEngine engine = new FraudRuleEngine();

    @Test
    void sendsHighRiskConcurrentRulesForReview() {
        var assessment = engine.assess(new FraudRuleEngine.TransactionSignal(new BigDecimal("24000"), true, 12, 14));
        assertThat(assessment.decision()).isEqualTo("REVIEW");
        assertThat(assessment.rules()).hasSize(4);
    }
}

