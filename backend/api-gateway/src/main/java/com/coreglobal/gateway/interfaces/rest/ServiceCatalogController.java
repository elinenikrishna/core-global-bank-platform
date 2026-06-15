package com.coreglobal.gateway.interfaces.rest;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/platform")
public class ServiceCatalogController {
    @GetMapping("/routes")
    public Map<String, Object> routes() {
        return Map.of("gateway", "core-global-api-gateway", "discovery", "docker-compose-dns",
                "routes", List.of(
                        route("/api/v1/auth/**", "auth-service:8081"),
                        route("/api/v1/customers/**", "customer-service:8082"),
                        route("/api/v1/accounts/**", "account-service:8083"),
                        route("/api/v1/transactions/**", "transaction-service:8084"),
                        route("/api/v1/transfers/**", "transfer-service:8085"),
                        route("/api/v1/cards/**", "card-service:8086"),
                        route("/api/v1/offers/**", "offers-service:8087"),
                        route("/api/v1/notifications/**", "notification-service:8088"),
                        route("/api/v1/fraud-alerts/**", "fraud-detection-service:8089"),
                        route("/api/v1/chatbot/**", "chatbot-service:8090"),
                        route("/api/v1/admin/**", "admin-service:8091")));
    }
    private Map<String, String> route(String path, String target) { return Map.of("path", path, "target", target); }
}

