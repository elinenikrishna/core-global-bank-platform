package com.coreglobal.admin.interfaces.rest;

import com.coreglobal.admin.application.SeedGenerationService;
import com.coreglobal.admin.application.SeedGenerationService.SeedRequest;
import com.coreglobal.admin.application.SeedGenerationService.SeedStatus;
import java.util.Map;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/seed-jobs")
public class SeedController {
    private final SeedGenerationService seeds;
    public SeedController(SeedGenerationService seeds) { this.seeds = seeds; }

    @PostMapping
    public Map<String, UUID> start(@RequestBody(required = false) SeedRequest request, @RequestParam(defaultValue = "false") boolean portfolioScale) {
        SeedRequest selected = portfolioScale ? SeedRequest.portfolioScale() : request == null ? SeedRequest.demo() : request;
        return Map.of("jobId", seeds.start(selected));
    }

    @GetMapping("/{jobId}")
    public SeedStatus status(@PathVariable UUID jobId) { return seeds.status(jobId); }
}
