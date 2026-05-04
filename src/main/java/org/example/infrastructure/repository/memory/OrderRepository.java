package org.example.infrastructure.repository.memory;

import org.example.domain.common.exception.EntityNotFoundException;
import org.example.domain.order.Order;
import org.example.domain.order.OrderId;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class OrderRepository implements org.example.domain.ports.OrderRepository {
    private static final Map<OrderId, Order> orders = new ConcurrentHashMap<>();

    @Override
    public List<Order> findAll() {
        return new ArrayList<>(orders.values());
    }

    @Override
    public Order findById(OrderId orderId) {
        return orders.get(orderId);
    }

    @Override
    public Order save(Order order) {
        orders.put(order.getId(), order);
        return order;
    }

    @Override
    public Order update(Order order) {
        OrderId newId = order.getId();

        if (!orders.containsKey(newId)) {
            throw new EntityNotFoundException("Order with id: " + order.getId().value() + " not found.");
        }

        orders.put(newId, order);
        return order;
    }

    @Override
    public void delete(OrderId orderId) {
        if (!orders.containsKey(orderId)) {
            throw new EntityNotFoundException("Order with id: " + orderId.value() + " not found.");
        }

        orders.remove(orderId);
    }
}
