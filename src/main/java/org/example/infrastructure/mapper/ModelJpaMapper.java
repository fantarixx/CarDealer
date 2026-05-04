package org.example.infrastructure.mapper;

import org.example.domain.car.*;
import org.example.domain.common.Money;
import org.example.domain.common.exception.EntityNotFoundException;
import org.example.domain.ports.ComponentRepository;
import org.example.infrastructure.persistence.entity.ModelJpaEntity;

import java.time.Year;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@org.springframework.stereotype.Component
public class ModelJpaMapper {
    public Model toDomain(ModelJpaEntity entity, ComponentRepository componentRepository) {
        if (entity == null) {
            return null;
        }

        Map<ComponentType, Component> standardComponents = new HashMap<>();
        if (entity.getStandardComponentIds() != null) {
            for (Map.Entry<ComponentType, UUID> entry : entity.getStandardComponentIds().entrySet()) {
                ComponentType type = entry.getKey();
                UUID compId = entry.getValue();
                Component component = componentRepository.findById(new ComponentId(compId));
                if (component != null) {
                    standardComponents.put(type, component);
                } else {
                    throw new EntityNotFoundException("Such components wasn't find.");
                }
            }
        }

        return new Model(
                new ModelId(entity.getId()),
                entity.getName(),
                entity.getBrand(),
                Money.of(entity.getBasePrice()),
                entity.getEngineType(),
                Year.of(entity.getYear()),
                entity.getBodyType(),
                entity.getDriveType(),
                entity.getTransmission(),
                entity.getSeatsCount(),
                standardComponents
        );
    }

    public ModelJpaEntity toJpa(Model model) {
        if (model == null) {
            return null;
        }

        ModelJpaEntity entity = new ModelJpaEntity();
        entity.setId(model.id().value());
        entity.setName(model.name());
        entity.setBrand(model.brand());
        entity.setBasePrice(model.basePrice().getAmountInCents());
        entity.setEngineType(model.engineType());
        entity.setYear(model.year().getValue());
        entity.setBodyType(model.bodyType());
        entity.setDriveType(model.driveType());
        entity.setTransmission(model.transmission());
        entity.setSeatsCount(model.seatsCount());

        Map<ComponentType, UUID> standardComponentIds = new HashMap<>();
        for (Map.Entry<ComponentType, Component> entry : model.standardComponents().entrySet()) {
            standardComponentIds.put(entry.getKey(), entry.getValue().id().value());
        }
        entity.setStandardComponentIds(standardComponentIds);

        return entity;
    }
}
