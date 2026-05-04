package org.example.infrastructure.persistence.repository;

import org.example.infrastructure.persistence.entity.OrderJpaEntity;
import org.example.domain.order.OrderState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderJpaRepository extends JpaRepository<OrderJpaEntity, UUID> {

    List<OrderJpaEntity> findByClientId(UUID clientId);

    List<OrderJpaEntity> findByCurrentState(OrderState state);
}