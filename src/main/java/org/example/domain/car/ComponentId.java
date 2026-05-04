package org.example.domain.car;

import java.util.UUID;

public record ComponentId(UUID value) {
    public ComponentId {
        if (value == null) {
            throw new IllegalArgumentException("CarCOmponentId can't be null");
        }
    }

    public ComponentId() {
        this(UUID.randomUUID());
    }
}