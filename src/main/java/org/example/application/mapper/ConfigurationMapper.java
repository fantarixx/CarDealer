package org.example.application.mapper;

import org.example.application.dto.configuration.CarConfigurationResponseDto;
import org.example.application.dto.configuration.ComponentResponseDto;
import org.example.domain.car.Model;
import org.example.domain.ports.ModelRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ConfigurationMapper {
    public ComponentResponseDto toComponentResponse(org.example.domain.car.Component component, ModelRepository repository) {
        return new ComponentResponseDto(
                component.id().value(),
                component.name(),
                component.price().getAmountInCents(),
                component.compatibleModels().stream()
                        .map(model -> repository.findById(model).name())
                        .collect(Collectors.toList())
        );
    }

    public CarConfigurationResponseDto toResponse(Model model,
                                                  List<org.example.domain.car.Component> components,
                                                  Long totalPrice, String status) {
        return new CarConfigurationResponseDto(
                model.name(),
                components.stream().map(org.example.domain.car.Component::name).collect(Collectors.toList()),
                totalPrice,
                status
        );
    }
}
