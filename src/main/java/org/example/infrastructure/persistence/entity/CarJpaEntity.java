package org.example.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.domain.car.ComponentType;

import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "cars")
@Getter
@Setter
public class CarJpaEntity extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "model_id")
    private ModelJpaEntity model;

    private String color;
    private boolean isAvailable;
    private boolean availableForTestDrive;

    @ElementCollection
    @CollectionTable(name = "car_components", joinColumns = @JoinColumn(name = "car_id"))
    @MapKeyColumn(name = "component_type")
    @MapKeyEnumerated(EnumType.STRING)
    @Column(name = "component_id")
    private Map<ComponentType, UUID> componentIds;
}
