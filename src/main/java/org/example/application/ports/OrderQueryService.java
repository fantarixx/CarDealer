package org.example.application.ports;

import org.example.application.dto.order.OrderResponseDto;

import java.util.List;
import java.util.UUID;

public interface OrderQueryService {
    OrderResponseDto getOrderById(UUID orderId);
    List<OrderResponseDto> getOrdersByClient(UUID clientId);
    List<OrderResponseDto> getAllOrders();
}