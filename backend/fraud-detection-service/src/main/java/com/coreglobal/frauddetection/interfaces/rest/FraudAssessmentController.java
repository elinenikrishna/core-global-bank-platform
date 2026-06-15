package com.coreglobal.frauddetection.interfaces.rest;

import com.coreglobal.frauddetection.application.FraudRuleEngine;
import com.coreglobal.frauddetection.application.FraudRuleEngine.Assessment;
import com.coreglobal.frauddetection.application.FraudRuleEngine.TransactionSignal;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/fraud-assessment")
public class FraudAssessmentController {
    private final FraudRuleEngine engine;
    public FraudAssessmentController(FraudRuleEngine engine) { this.engine = engine; }
    @PostMapping public Assessment assess(@Valid @RequestBody TransactionSignal signal) { return engine.assess(signal); }
}

