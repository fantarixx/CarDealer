package org.example.application.dto.configuration;

import java.util.List;
import java.util.UUID;

public record ComponentResponseDto(
        UUID id,
        String name,
        long price,
        List<String> compatibleModels
) {
}
