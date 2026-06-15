package com.coreglobal.common.api;

import java.time.Instant;
import java.util.UUID;

public record CommandResponse(UUID commandId, String status, String topic, Instant acceptedAt) {
}

