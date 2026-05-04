package org.example.domain.order;

import java.util.UUID;

public record TestDriveId(UUID value) {
    public TestDriveId {
        if (value == null) {
            throw new IllegalArgumentException("TestDriveId can't be null");
        }
    }

    public TestDriveId() {
        this(UUID.randomUUID());
    }
}
