package org.example.infrastructure.repository.jpa;

import lombok.AllArgsConstructor;
import org.example.domain.order.Order;
import org.example.domain.order.OrderId;
import org.example.domain.common.exception.EntityNotFoundException;
import org.example.domain.ports.*;
import org.example.infrastructure.mapper.OrderJpaMapper;
import org.example.infrastructure.persistence.entity.OrderJpaEntity;
import org.example.infrastructure.persistence.repository.OrderJpaRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Primary
@Repository
public class JpaOrderRepository implements OrderRepository {

    private final OrderJpaRepository jpaRepository;
    private final OrderJpaMapper mapper;
    private final CarRepository carRepository;
    private final ModelRepository modelRepository;
    private final ComponentRepository componentRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Order> findAll() {
        return jpaRepository.findAll().stream()
                .map(entity -> mapper.toDomain(entity, carRepository, modelRepository, componentRepository))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Order findById(OrderId orderId) {
        Optional<OrderJpaEntity> entity = jpaRepository.findById(orderId.value());
        return entity.map(e -> mapper.toDomain(e, carRepository, modelRepository, componentRepository)).orElse(null);
    }

    @Override
    @Transactional
    public Order save(Order order) {
        OrderJpaEntity entity = mapper.toJpa(order);
        OrderJpaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved, carRepository, modelRepository, componentRepository);
    }

    @Override
    @Transactional
    public Order update(Order order) {
        if (!jpaRepository.existsById(order.getId().value())) {
            throw new EntityNotFoundException("Order with id " + order.getId().value() + " not found");
        }
        OrderJpaEntity entity = mapper.toJpa(order);
        OrderJpaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved, carRepository, modelRepository, componentRepository);
    }

    @Override
    @Transactional
    public void delete(OrderId orderId) {
        if (!jpaRepository.existsById(orderId.value())) {
            throw new EntityNotFoundException("Order with id " + orderId.value() + " not found");
        }
        jpaRepository.deleteById(orderId.value());
    }
}