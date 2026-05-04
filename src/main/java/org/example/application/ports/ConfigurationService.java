package org.example.application.ports;

import org.example.application.dto.configuration.CarConfigureRequestDto;
import org.example.application.dto.configuration.CarConfigurationResponseDto;
import org.example.application.dto.configuration.ComponentResponseDto;
import org.example.domain.car.Model;
import org.example.domain.car.Component;

import java.util.List;

public interface ConfigurationService {
    CarConfigurationResponseDto configureCar(CarConfigureRequestDto request);
    public List<ComponentResponseDto> getCompatibleComponents(String modelName);
    long calculateTotalPrice(Model model, List<Component> components);
}
