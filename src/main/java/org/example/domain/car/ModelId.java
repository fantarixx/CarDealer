package org.example.domain.car;

import java.util.UUID;

public record ModelId(UUID value) {
    public ModelId {
        if (value == null) {
            throw new IllegalArgumentException("CarModelId can't be null");
        }
    }

    public ModelId() {
        this(UUID.randomUUID());
    }
}
