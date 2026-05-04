package org.example.application.ports;

import org.example.application.dto.model.CarModelCreateRequestDto;
import org.example.application.dto.model.CarModelUpdateRequestDto;
import org.example.application.dto.model.CarModelResponseDto;

import java.util.UUID;

public interface ModelCommandService {
    CarModelResponseDto createModel(CarModelCreateRequestDto request);
    CarModelResponseDto updateModel(CarModelUpdateRequestDto request);
    void deleteModel(UUID modelId);
}