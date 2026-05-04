package org.example.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.domain.order.OrderState;
import org.example.domain.order.OrderType;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class OrderJpaEntity extends BaseEntity {
    @Enumerated(EnumType.STRING)
    private OrderType type;

    @Enumerated(EnumType.STRING)
    private OrderState currentState;

    private UUID clientId;
    private UUID managerId;

    private UUID basicCarId;

    private UUID modelId;
    @Column(name = "car_color")
    private String carColor;
    @ElementCollection
    @CollectionTable(name = "order_selected_components", joinColumns = @JoinColumn(name = "order_id"))
    @Column(name = "component_id")
    private Set<UUID> selectedComponentIds;

    private long totalPrice; // в копейках

    private Instant confirmedAt;
    private Instant paidAt;
    private Instant completedAt;
    private Instant cancelledAt;
}