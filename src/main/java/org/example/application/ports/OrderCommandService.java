package org.example.application.ports;

import org.example.application.dto.order.OrderCreateBasicRequestDto;
import org.example.application.dto.order.OrderCreateSpecificRequestDto;
import org.example.application.dto.order.OrderResponseDto;

import java.util.UUID;

public interface OrderCommandService {
    OrderResponseDto createBasicOrder(OrderCreateBasicRequestDto request);
    OrderResponseDto createSpecificOrder(OrderCreateSpecificRequestDto request);
    OrderResponseDto confirmOrder(UUID orderId);
    OrderResponseDto payOrder(UUID orderId);
    OrderResponseDto markReady(UUID orderId);
    OrderResponseDto completeOrder(UUID orderId);
    OrderResponseDto cancelOrder(UUID orderId);
}