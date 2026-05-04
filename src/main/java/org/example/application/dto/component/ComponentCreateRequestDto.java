package org.example.application.dto.component;

import java.util.List;
import java.util.UUID;

public record ComponentCreateRequestDto(
        String type,
        String name,
        long price,
        List<UUID> compatibleModelIds
) {
}