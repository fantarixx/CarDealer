package org.example.application.dto.component;

import java.util.List;
import java.util.UUID;

public record ComponentResponseDto(
        UUID id,
        String type,
        String name,
        long price,
        List<String> compatibleModels
) {
}