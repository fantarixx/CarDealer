package org.example.application.dto.order;

import java.time.LocalDateTime;
import java.util.UUID;

public record TestDriveResponseDto(
        UUID id,
        UUID clientId,
        UUID carId,
        LocalDateTime scheduledAt,
        String state
) {
}
