package org.example.domain.car;

import org.example.domain.common.Money;

import java.util.Set;

public record Component(
        ComponentId id,
        ComponentType type,
        String name,
        Money price,
        Set<ModelId> compatibleModels
) {
}

