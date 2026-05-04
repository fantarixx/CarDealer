package org.example.infrastructure.mapper;

import org.example.domain.car.*;
import org.example.domain.common.Money;
import org.example.domain.ports.ModelRepository;
import org.example.infrastructure.persistence.entity.ComponentJpaEntity;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ComponentJpaMapper {
    public org.example.domain.car.Component toDomain(ComponentJpaEntity entity, ModelRepository modelRepository) {
        if (entity == null) {
            return null;
        }

        Set<ModelId> compatibleModels = new HashSet<>();
        if (entity.getCompatibleModelIds() != null) {
            for (UUID modelId : entity.getCompatibleModelIds()) {
                Model model = modelRepository.findById(new ModelId(modelId));
                if (model != null) {
                    compatibleModels.add(model.id());
                }
            }
        }

        return new org.example.domain.car.Component(
                new ComponentId(entity.getId()),
                entity.getType(),
                entity.getName(),
                Money.of(entity.getPrice()),
                compatibleModels
        );
    }

    public ComponentJpaEntity toJpa(org.example.domain.car.Component component) {
        if (component == null) {
            return null;
        }

        ComponentJpaEntity entity = new ComponentJpaEntity();
        entity.setId(component.id().value());
        entity.setType(component.type());
        entity.setName(component.name());
        entity.setPrice(component.price().getAmountInCents());

        Set<UUID> compatibleModelIds = component.compatibleModels().stream().map(ModelId::value).collect(Collectors.toSet());
        entity.setCompatibleModelIds(compatibleModelIds);

        return entity;
    }
}