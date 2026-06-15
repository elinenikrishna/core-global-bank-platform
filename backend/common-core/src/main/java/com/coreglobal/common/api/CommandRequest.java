package com.coreglobal.common.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Map;

public record CommandRequest(
        @NotBlank @Size(max = 80) String action,
        @Size(max = 120) String ownerId,
        BigDecimal amount,
        Map<String, String> attributes) {
}

