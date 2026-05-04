package org.example.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.domain.order.TestDriveState;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "test_drives")
@Getter
@Setter
public class TestDriveJpaEntity extends BaseEntity {
    private UUID clientId;
    private UUID carId;
    private Instant scheduledAt;

    @Enumerated(EnumType.STRING)
    private TestDriveState state;
}