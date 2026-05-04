package org.example.application.dto.car;

import java.util.List;

public record CarSearchResponseDto(
        String id,
        String brand,
        String model,
        String fullName,
        Long totalPrice,
        boolean isAvailable,
        int year,
        String engineType,
        String bodyType,
        List<String> components
) {
}
