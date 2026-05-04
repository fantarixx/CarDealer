package org.example.application.dto.car;

public record CarCommandDto(
        String modelName,
        Boolean initiallyAvailable,
        String color
) {
}