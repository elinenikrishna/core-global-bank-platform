package com.coreglobal.auth.interfaces.rest;

import com.coreglobal.common.api.ApiEnvelope;
import com.coreglobal.common.api.CommandRequest;
import com.coreglobal.common.api.CommandResponse;
import com.coreglobal.common.observability.CorrelationIdFilter;
import com.coreglobal.auth.application.AuthApplicationService;
import com.coreglobal.auth.domain.model.AuthRecord;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthApplicationService service;
    public AuthController(AuthApplicationService service) { this.service = service; }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ApiEnvelope<Page<AuthRecord>> find(@RequestParam(required = false) String owner, Pageable pageable, HttpServletRequest request) {
        return ApiEnvelope.of(service.find(owner, pageable), request.getHeader(CorrelationIdFilter.HEADER));
    }

    @GetMapping("/summary")
    public Map<String, Object> summary() { return service.summary(); }

    @PostMapping("/actions")
    public ApiEnvelope<CommandResponse> execute(@Valid @RequestBody CommandRequest command, HttpServletRequest request) {
        return ApiEnvelope.of(service.execute(command), request.getHeader(CorrelationIdFilter.HEADER));
    }
}
