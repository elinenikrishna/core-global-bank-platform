#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/../backend" && pwd)"

services=(
  "api-gateway:Gateway:gateway:gateway-events:8080"
  "auth-service:Auth:auth:notification-created:8081"
  "customer-service:Customer:customers:customer-updated:8082"
  "account-service:Account:accounts:account-updated:8083"
  "transaction-service:Transaction:transactions:transaction-created:8084"
  "transfer-service:Transfer:transfers:transfer-requested:8085"
  "card-service:Card:cards:card-status-updated:8086"
  "offers-service:Offer:offers:offer-activated:8087"
  "notification-service:Notification:notifications:notification-created:8088"
  "fraud-detection-service:FraudAlert:fraud-alerts:fraud-alert-created:8089"
  "chatbot-service:Chatbot:chatbot:chatbot-message-created:8090"
  "admin-service:Admin:admin:admin-audit-created:8091"
)

for item in "${services[@]}"; do
  IFS=: read -r module class resource topic port <<< "$item"
  package="$(echo "$module" | sed 's/-service$//' | tr -d '-')"
  [[ "$module" == "api-gateway" ]] && package="gateway"
  base="$ROOT/$module"
  java="$base/src/main/java/com/coreglobal/$package"
  mkdir -p "$java/domain/model" "$java/domain/repository" "$java/application" "$java/interfaces/rest" "$java/config" "$base/src/main/resources" "$base/src/test/java/com/coreglobal/$package/application"

  cat > "$base/pom.xml" <<EOF
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <parent><groupId>com.coreglobal</groupId><artifactId>core-global-platform</artifactId><version>1.0.0</version></parent>
  <artifactId>$module</artifactId>
  <dependencies>
    <dependency><groupId>com.coreglobal</groupId><artifactId>common-core</artifactId><version>\${project.version}</version></dependency>
    <dependency><groupId>com.h2database</groupId><artifactId>h2</artifactId><scope>runtime</scope></dependency>
    <dependency><groupId>org.postgresql</groupId><artifactId>postgresql</artifactId><scope>runtime</scope></dependency>
    <dependency><groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-test</artifactId><scope>test</scope></dependency>
    <dependency><groupId>org.mockito</groupId><artifactId>mockito-junit-jupiter</artifactId><scope>test</scope></dependency>
  </dependencies>
  <build><plugins><plugin><groupId>org.springframework.boot</groupId><artifactId>spring-boot-maven-plugin</artifactId></plugin></plugins></build>
</project>
EOF

  cat > "$java/${class}ServiceApplication.java" <<EOF
package com.coreglobal.$package;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.coreglobal")
public class ${class}ServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(${class}ServiceApplication.class, args);
    }
}
EOF

  cat > "$java/domain/model/${class}Record.java" <<EOF
package com.coreglobal.$package.domain.model;

import com.coreglobal.common.domain.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "CGB_${resource//-/_}", indexes = {
    @Index(name = "IDX_${package^^}_OWNER", columnList = "ownerId"),
    @Index(name = "IDX_${package^^}_STATUS", columnList = "status")
})
public class ${class}Record extends AuditableEntity {
    @Column(nullable = false, unique = true) private String reference;
    @Column(nullable = false) private String ownerId;
    @Column(nullable = false) private String status;
    @Column(precision = 19, scale = 4) private BigDecimal amount;
    @Column(length = 2000) private String details;

    protected ${class}Record() {}
    public ${class}Record(String reference, String ownerId, String status, BigDecimal amount, String details) {
        this.reference = reference; this.ownerId = ownerId; this.status = status; this.amount = amount; this.details = details;
    }
    public String getReference() { return reference; }
    public String getOwnerId() { return ownerId; }
    public String getStatus() { return status; }
    public BigDecimal getAmount() { return amount; }
    public String getDetails() { return details; }
    public void updateStatus(String status) { this.status = status; }
}
EOF

  cat > "$java/domain/repository/${class}Repository.java" <<EOF
package com.coreglobal.$package.domain.repository;

import com.coreglobal.$package.domain.model.${class}Record;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ${class}Repository extends JpaRepository<${class}Record, UUID> {
    Page<${class}Record> findByOwnerIdContainingIgnoreCase(String ownerId, Pageable pageable);
}
EOF

  cat > "$java/application/${class}ApplicationService.java" <<EOF
package com.coreglobal.$package.application;

import com.coreglobal.common.api.CommandRequest;
import com.coreglobal.common.api.CommandResponse;
import com.coreglobal.common.event.BankingEventPublisher;
import com.coreglobal.$package.domain.model.${class}Record;
import com.coreglobal.$package.domain.repository.${class}Repository;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ${class}ApplicationService {
    private final ${class}Repository repository;
    private final BankingEventPublisher events;
    public ${class}ApplicationService(${class}Repository repository, BankingEventPublisher events) { this.repository = repository; this.events = events; }

    @Transactional(readOnly = true)
    public Page<${class}Record> find(String owner, Pageable pageable) {
        return owner == null || owner.isBlank() ? repository.findAll(pageable) : repository.findByOwnerIdContainingIgnoreCase(owner, pageable);
    }

    @Transactional
    public CommandResponse execute(CommandRequest command) {
        String reference = "${package^^}-" + UUID.randomUUID().toString().substring(0, 8);
        ${class}Record saved = repository.save(new ${class}Record(reference, command.ownerId() == null ? "demo-customer" : command.ownerId(), "ACCEPTED", command.amount(), String.valueOf(command.attributes())));
        UUID eventId = events.publish("$topic", command.action(), Map.of("reference", saved.getReference(), "ownerId", saved.getOwnerId()));
        return new CommandResponse(eventId, "ACCEPTED", "$topic", Instant.now());
    }

    public Map<String, Object> summary() {
        return Map.of("service", "$module", "status", "operational", "recordCount", repository.count(), "eventTopic", "$topic", "timestamp", Instant.now());
    }
}
EOF

  cat > "$java/interfaces/rest/${class}Controller.java" <<EOF
package com.coreglobal.$package.interfaces.rest;

import com.coreglobal.common.api.ApiEnvelope;
import com.coreglobal.common.api.CommandRequest;
import com.coreglobal.common.api.CommandResponse;
import com.coreglobal.common.observability.CorrelationIdFilter;
import com.coreglobal.$package.application.${class}ApplicationService;
import com.coreglobal.$package.domain.model.${class}Record;
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
@RequestMapping("/api/v1/$resource")
public class ${class}Controller {
    private final ${class}ApplicationService service;
    public ${class}Controller(${class}ApplicationService service) { this.service = service; }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ApiEnvelope<Page<${class}Record>> find(@RequestParam(required = false) String owner, Pageable pageable, HttpServletRequest request) {
        return ApiEnvelope.of(service.find(owner, pageable), request.getHeader(CorrelationIdFilter.HEADER));
    }

    @GetMapping("/summary")
    public Map<String, Object> summary() { return service.summary(); }

    @PostMapping("/actions")
    public ApiEnvelope<CommandResponse> execute(@Valid @RequestBody CommandRequest command, HttpServletRequest request) {
        return ApiEnvelope.of(service.execute(command), request.getHeader(CorrelationIdFilter.HEADER));
    }
}
EOF

  cat > "$base/src/main/resources/application.yml" <<EOF
server:
  port: \${SERVER_PORT:$port}
  servlet:
    context-path: /
spring:
  application:
    name: $module
  datasource:
    url: \${DATABASE_URL:jdbc:h2:mem:${package};MODE=Oracle;DB_CLOSE_DELAY=-1}
    username: \${DATABASE_USERNAME:sa}
    password: \${DATABASE_PASSWORD:}
  jpa:
    hibernate:
      ddl-auto: \${DDL_AUTO:update}
    open-in-view: false
    properties:
      hibernate:
        default_schema: \${DATABASE_SCHEMA:PUBLIC}
  kafka:
    bootstrap-servers: \${KAFKA_BOOTSTRAP_SERVERS:localhost:9092}
core:
  kafka:
    enabled: \${KAFKA_ENABLED:false}
management:
  endpoints:
    web:
      exposure:
        include: health,info,prometheus,metrics,loggers
  endpoint:
    health:
      show-details: always
springdoc:
  swagger-ui:
    path: /swagger-ui.html
logging:
  pattern:
    console: '{"timestamp":"%d{yyyy-MM-dd''T''HH:mm:ss.SSSXXX}","level":"%level","service":"$module","correlationId":"%X{correlationId}","thread":"%thread","logger":"%logger{24}","message":"%msg"}%n'
EOF

  cat > "$base/Dockerfile" <<EOF
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/$module-1.0.0.jar app.jar
USER 10001
EXPOSE $port
ENTRYPOINT ["java","-XX:MaxRAMPercentage=75.0","-jar","app.jar"]
EOF
done

echo "Scaffolded ${#services[@]} Spring Boot services."
