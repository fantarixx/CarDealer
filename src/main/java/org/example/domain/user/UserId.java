package org.example.domain.user;

import java.util.UUID;

public record UserId(UUID value) {
    public UserId {
        if (value == null) {
            throw new IllegalArgumentException("UserId can't be null");
        }
    }

    public UserId() {
        this(UUID.randomUUID());
    }
}