package org.example.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.domain.car.ComponentType;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "components")
@Getter
@Setter
public class ComponentJpaEntity extends BaseEntity {
    @Enumerated(EnumType.STRING)
    private ComponentType type;

    private String name;
    private long price;

    @ElementCollection
    @CollectionTable(name = "component_compatible_models", joinColumns = @JoinColumn(name = "component_id"))
    @Column(name = "model_id")
    private Set<UUID> compatibleModelIds = new HashSet<>();
}