package org.example.application.mapper;

import org.example.application.dto.model.CarModelCreateRequestDto;
import org.example.application.dto.model.CarModelResponseDto;
import org.example.domain.car.*;
import org.example.domain.common.Money;
import org.example.domain.common.exception.DomainValidationException;
import org.example.domain.ports.ComponentRepository;
import org.springframework.stereotype.Component;

import java.time.Year;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class CarModelMapper {

    public Model toEntity(CarModelCreateRequestDto request, ComponentRepository componentRepository) {
        EngineType engineType = parseEnum(EngineType.class, request.engineType(), "EngineType");
        BodyType bodyType = parseEnum(BodyType.class, request.bodyType(), "BodyType");
        DriveType driveType = parseEnum(DriveType.class, request.driveType(), "DriveType");
        TransmissionType transmission = parseEnum(TransmissionType.class, request.transmission(), "TransmissionType");

        Money basePrice = Money.of(request.basePrice());
        Year year = Year.of(request.year());

        Map<ComponentType, org.example.domain.car.Component> standard = new HashMap<>();
        for (Map.Entry<String, UUID> entry : request.standardComponents().entrySet()) {
            ComponentType type = parseEnum(ComponentType.class, entry.getKey(), "ComponentType");
            org.example.domain.car.Component component = componentRepository.findById(new ComponentId(entry.getValue()));
            if (component == null) {
                throw new DomainValidationException("Component not found: " + entry.getValue());
            }
            standard.put(type, component);
        }

        return new Model(
                new ModelId(),
                request.name(),
                request.brand(),
                basePrice,
                engineType,
                year,
                bodyType,
                driveType,
                transmission,
                request.seatsCount(),
                standard
        );
    }

    public CarModelResponseDto toDto(Model model) {
        Map<String, UUID> standardMap = model.standardComponents().entrySet().stream()
                .collect(Collectors.toMap(
                        e -> e.getKey().name(),
                        e -> e.getValue().id().value()
                ));
        return new CarModelResponseDto(
                model.id().value(),
                model.name(),
                model.brand(),
                model.basePrice().getAmountInCents(),
                model.engineType().name(),
                model.year().getValue(),
                model.bodyType().name(),
                model.driveType().name(),
                model.transmission().name(),
                model.seatsCount(),
                standardMap
        );
    }

    private <T extends Enum<T>> T parseEnum(Class<T> enumClass, String value, String enumName) {
        try {
            return Enum.valueOf(enumClass, value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new DomainValidationException("Invalid " + enumName + ": " + value);
        }
    }
}