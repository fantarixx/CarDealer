package org.example.domain.order;

import java.util.UUID;

public record OrderId(UUID value) {
    public OrderId {
        if (value == null) {
            throw new IllegalArgumentException("OrderId can't be null");
        }
    }

    public OrderId() {
        this(UUID.randomUUID());
    }
}

