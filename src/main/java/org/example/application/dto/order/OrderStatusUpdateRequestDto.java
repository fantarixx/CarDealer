package org.example.application.dto.order;

import java.util.UUID;

public record OrderStatusUpdateRequestDto(
        UUID orderId
) {
}