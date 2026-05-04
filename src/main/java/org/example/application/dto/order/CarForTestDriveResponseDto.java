package org.example.application.dto.order;

import java.util.UUID;

public record CarForTestDriveResponseDto(
        UUID carId,
        String brand,
        String model,
        String fullName
) {
}