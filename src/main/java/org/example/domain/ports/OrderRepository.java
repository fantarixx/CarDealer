package org.example.domain.ports;

import org.example.domain.order.Order;
import org.example.domain.order.OrderId;

import java.util.List;

public interface OrderRepository {
    List<Order> findAll();
    Order findById(OrderId orderId);
    Order save(Order order);
    Order update(Order order);
    void delete(OrderId orderId);
}
