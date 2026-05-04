package org.example.infrastructure.mapper;

import org.example.domain.car.*;
import org.example.domain.common.Color;
import org.example.domain.order.Order;
import org.example.domain.order.OrderId;
import org.example.domain.order.OrderType;
import org.example.domain.ports.CarRepository;
import org.example.domain.ports.ComponentRepository;
import org.example.domain.ports.ModelRepository;
import org.example.domain.user.UserId;
import org.example.infrastructure.persistence.entity.OrderJpaEntity;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@org.springframework.stereotype.Component
public class OrderJpaMapper {
    public Order toDomain(OrderJpaEntity entity, CarRepository carRepository,
                          ModelRepository modelRepository, ComponentRepository componentRepository) {
        if (entity == null) {
            return null;
        }

        Order order;
        if (entity.getType() == OrderType.BASIC) {
            Car car = carRepository.findById(new CarId(entity.getBasicCarId()));
            if (car == null) {
                return null;
            }
            order = new Order(new OrderId(entity.getId()),
                    entity.getType(),
                    new UserId(entity.getManagerId()),
                    car,
                    new UserId(entity.getClientId()));
        } else {
            Model model = modelRepository.findById(new ModelId(entity.getModelId()));
            if (model == null) {
                return null;
            }

            Set<Component> components = entity.getSelectedComponentIds().stream()
                    .map(id -> componentRepository.findById(new ComponentId(id)))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            Car configuredCar = new Car(new CarId(), model, new Color(entity.getCarColor()));
            components.forEach(configuredCar::addComponent);
            configuredCar.setAvailable(false);

            order = new Order(new OrderId(entity.getId()),
                    entity.getType(),
                    new UserId(entity.getManagerId()),
                    configuredCar,
                    new UserId(entity.getClientId()));
        }

        order.setCurrentState(entity.getCurrentState());
        order.setCreatedAt(LocalDateTime.ofInstant(entity.getCreatedAt(), ZoneId.systemDefault()));
        order.setConfirmedAt(LocalDateTime.ofInstant(entity.getConfirmedAt(), ZoneId.systemDefault()));
        order.setPaidAt(LocalDateTime.ofInstant(entity.getPaidAt(), ZoneId.systemDefault()));
        order.setCompletedAt(LocalDateTime.ofInstant(entity.getCompletedAt(), ZoneId.systemDefault()));
        order.setCancelledAt(LocalDateTime.ofInstant(entity.getCancelledAt(), ZoneId.systemDefault()));

        return order;
    }

    public OrderJpaEntity toJpa(Order order) {
        if (order == null) {
            return null;
        }

        OrderJpaEntity entity = new OrderJpaEntity();
        entity.setId(order.getId().value());
        entity.setType(order.getType());
        entity.setCurrentState(order.getCurrentState());
        entity.setClientId(order.getClientId().value());
        entity.setManagerId(order.getManagerId().value());
        entity.setTotalPrice(order.getTotalPrice().getAmountInCents());
        entity.setCarColor(order.getCar().getColor().toString());

        entity.setCreatedAt(order.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant());
        entity.setConfirmedAt(order.getConfirmedAt().atZone(ZoneId.systemDefault()).toInstant());
        entity.setPaidAt(order.getPaidAt().atZone(ZoneId.systemDefault()).toInstant());
        entity.setCompletedAt(order.getCompletedAt().atZone(ZoneId.systemDefault()).toInstant());
        entity.setCancelledAt(order.getCancelledAt().atZone(ZoneId.systemDefault()).toInstant());

        if (order.getType() == OrderType.BASIC) {
            entity.setBasicCarId(order.getCar().getId().value());
        } else {
            entity.setModelId(order.getCar().getModel().id().value());
            Set<UUID> componentIds = order.getCar().getComponents().values().stream()
                    .map(c -> c.id().value())
                    .collect(Collectors.toSet());
            entity.setSelectedComponentIds(componentIds);
        }

        return entity;
    }
}