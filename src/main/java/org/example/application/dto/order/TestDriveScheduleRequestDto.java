package org.example.application.dto.order;

import java.time.LocalDateTime;
import java.util.UUID;

public record TestDriveScheduleRequestDto(
        UUID carId,
        LocalDateTime scheduledAt
) {
}
