package org.example.application.dto.component;

import java.util.UUID;

public record ComponentUpdateRequestDto(
        UUID id,
        String name,
        Long price
) {
}