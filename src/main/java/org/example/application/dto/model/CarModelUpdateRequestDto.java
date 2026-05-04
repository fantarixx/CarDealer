package org.example.application.dto.model;

import java.util.Map;
import java.util.UUID;

public record CarModelUpdateRequestDto(
        UUID id,
        String name,
        String brand,
        Long basePrice,
        String engineType,
        Integer year,
        String bodyType,
        String driveType,
        String transmission,
        Integer seatsCount,
        Map<String, UUID> standardComponents
) {
}