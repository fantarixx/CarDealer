package org.example.application.dto.order;

import java.util.List;
import java.util.UUID;

public record OrderCreateSpecificRequestDto(
        String modelName,
        String color,
        List<UUID> componentIds
) {
}