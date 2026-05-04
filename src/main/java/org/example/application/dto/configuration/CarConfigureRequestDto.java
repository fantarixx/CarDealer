package org.example.application.dto.configuration;

import java.util.List;
import java.util.UUID;

public record CarConfigureRequestDto(
            String modelName,
            List<UUID> componentIds
) {
}
