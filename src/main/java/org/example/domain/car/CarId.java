package org.example.domain.car;

import java.util.UUID;

public record CarId(UUID value) {
    public CarId {
        if (value == null) {
            throw new IllegalArgumentException("CarId can't be null");
        }
    }

    public CarId() {
        this(UUID.randomUUID());
    }
}
