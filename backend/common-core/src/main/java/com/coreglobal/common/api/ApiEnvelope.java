package com.coreglobal.common.api;

import java.time.Instant;

public record ApiEnvelope<T>(T data, String correlationId, Instant timestamp) {
    public static <T> ApiEnvelope<T> of(T data, String correlationId) {
        return new ApiEnvelope<>(data, correlationId, Instant.now());
    }
}

