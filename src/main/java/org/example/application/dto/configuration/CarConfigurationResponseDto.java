package org.example.application.dto.configuration;

import java.util.List;

public record CarConfigurationResponseDto(
        String modelName,
        List<String> components,
        Long totalPrice,
        String status
) {
}
