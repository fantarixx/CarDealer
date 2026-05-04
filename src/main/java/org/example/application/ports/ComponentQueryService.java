package org.example.application.ports;

import org.example.application.dto.component.ComponentResponseDto;

import java.util.List;
import java.util.UUID;

public interface ComponentQueryService {
    ComponentResponseDto getComponentById(UUID componentId);
    List<ComponentResponseDto> getAllComponents();
    List<ComponentResponseDto> getComponentsByType(String type);
    List<ComponentResponseDto> getCompatibleComponentsForModel(UUID modelId);
}