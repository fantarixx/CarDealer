package org.example.application.dto.car;

public record CarSearchRequestDto(
        String brand,
        String model,
        Long minPrice,
        Long maxPrice,
        Integer minYear,
        Integer maxYear,
        Boolean availableOnly
) {
}
