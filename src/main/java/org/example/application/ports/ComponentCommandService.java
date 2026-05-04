package org.example.application.ports;

import org.example.application.dto.component.ComponentCreateRequestDto;
import org.example.application.dto.component.ComponentUpdateRequestDto;
import org.example.application.dto.component.ComponentResponseDto;

import java.util.UUID;

public interface ComponentCommandService {
    ComponentResponseDto createComponent(ComponentCreateRequestDto request);
    ComponentResponseDto updateComponent(ComponentUpdateRequestDto request);
    void deleteComponent(UUID componentId);
    void addCompatibility(UUID componentId, UUID modelId);
    void removeCompatibility(UUID componentId, UUID modelId);
}