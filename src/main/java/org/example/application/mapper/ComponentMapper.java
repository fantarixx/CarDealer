package org.example.application.mapper;

import org.example.application.dto.component.ComponentResponseDto;
import org.example.domain.car.ComponentType;
import org.example.domain.ports.ModelRepository;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ComponentMapper {

    public ComponentResponseDto toDto(org.example.domain.car.Component component, ModelRepository repository) {
        return new ComponentResponseDto(
                component.id().value(),
                component.type().name(),
                component.name(),
                component.price().getAmountInCents(),
                component.compatibleModels().stream()
                        .map(model -> repository.findById(model).brand() + " " + repository.findById(model).name())
                        .collect(Collectors.toList())
        );
    }

    public ComponentType toType(String typeName) {
        return ComponentType.valueOf(typeName.toUpperCase());
    }
}