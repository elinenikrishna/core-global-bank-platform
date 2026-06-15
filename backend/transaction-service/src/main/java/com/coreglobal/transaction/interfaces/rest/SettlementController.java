package com.coreglobal.transaction.interfaces.rest;

import com.coreglobal.transaction.application.ConcurrentSettlementProcessor;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transactions/settlement-batches")
public class SettlementController {
    private final ConcurrentSettlementProcessor processor;
    public SettlementController(ConcurrentSettlementProcessor processor) { this.processor = processor; }
    @PostMapping public ConcurrentSettlementProcessor.BatchResult settle(@RequestBody List<String> references) { return processor.settle(references); }
}
