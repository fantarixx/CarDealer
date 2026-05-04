package org.example.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.domain.car.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Entity
@Table(name = "models")
@Getter
@Setter
public class ModelJpaEntity extends BaseEntity {
    private String name;
    private String brand;
    private long basePrice;

    @Enumerated(EnumType.STRING)
    private EngineType engineType;

    private int year;

    @Enumerated(EnumType.STRING)
    private BodyType bodyType;

    @Enumerated(EnumType.STRING)
    private DriveType driveType;

    @Enumerated(EnumType.STRING)
    private TransmissionType transmission;

    private int seatsCount;

    @ElementCollection
    @CollectionTable(name = "model_standard_components", joinColumns = @JoinColumn(name = "model_id"))
    @MapKeyColumn(name = "component_type")
    @MapKeyEnumerated(EnumType.STRING)
    @Column(name = "component_id")
    private Map<ComponentType, UUID> standardComponentIds = new HashMap<>();
}
