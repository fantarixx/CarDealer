package org.example.application.dto.model;

import java.util.Map;
import java.util.UUID;

public record CarModelCreateRequestDto(
        String name,
        String brand,
        long basePrice,
        String engineType,
        int year,
        String bodyType,
        String driveType,
        String transmission,
        int seatsCount,
        Map<String, UUID> standardComponents
) {
}