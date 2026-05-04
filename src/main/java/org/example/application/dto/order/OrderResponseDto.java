package org.example.application.dto.order;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderResponseDto(
        UUID id,
        String type,
        String state,
        Long totalPrice,
        UUID clientId,
        UUID managerId,
        String color,
        LocalDateTime createdAt,
        LocalDateTime confirmedAt,
        LocalDateTime paidAt,
        LocalDateTime completedAt,
        LocalDateTime cancelledAt,
        UUID carId
) {
}