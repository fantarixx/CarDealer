package org.example.application.ports;

import org.example.application.dto.model.CarModelResponseDto;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ModelQueryService {
    CarModelResponseDto getModelById(UUID modelId);
    List<CarModelResponseDto> getAllModels();
    List<CarModelResponseDto> getModels(String brand, Set<UUID> componentIds);
    CarModelResponseDto getModelByName(String name);
}