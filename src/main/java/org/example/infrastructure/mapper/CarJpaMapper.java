package org.example.infrastructure.mapper;

import org.example.domain.car.*;
import org.example.domain.common.Color;
import org.example.domain.ports.ComponentRepository;
import org.example.infrastructure.persistence.entity.CarJpaEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@org.springframework.stereotype.Component
public class CarJpaMapper {

    private final ModelJpaMapper modelMapper;
    private final ComponentJpaMapper componentMapper;

    public CarJpaMapper(ModelJpaMapper modelMapper, ComponentJpaMapper componentMapper) {
        this.modelMapper = modelMapper;
        this.componentMapper = componentMapper;
    }

    public Car toDomain(CarJpaEntity entity, ComponentRepository componentRepository) {
        if (entity == null) {
            return null;
        }
        Model model = modelMapper.toDomain(entity.getModel(), componentRepository);
        Car car = new Car(new CarId(entity.getId()), model, new Color(entity.getColor()));
        car.setAvailable(entity.isAvailable());
        car.setAvailableForTestDrive(entity.isAvailableForTestDrive());
        Map<ComponentType, Component> components = new HashMap<>();
        for (Map.Entry<ComponentType, UUID> entry : entity.getComponentIds().entrySet()) {
            Component comp = componentRepository.findById(new ComponentId(entry.getValue()));
            if (comp != null) {
                components.put(entry.getKey(), comp);
            }
        }
        components.values().forEach(car::addComponent);
        return car;
    }

    public CarJpaEntity toJpa(Car domain) {
        CarJpaEntity entity = new CarJpaEntity();
        entity.setId(domain.getId().value());
        entity.setModel(modelMapper.toJpa(domain.getModel()));
        entity.setColor(domain.getColor().toString());
        entity.setAvailable(domain.isAvailable());
        entity.setAvailableForTestDrive(domain.isAvailableForTestDrive());

        Map<ComponentType, UUID> componentIds = domain.getComponents().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().id().value()));
        entity.setComponentIds(componentIds);
        return entity;
    }
}
