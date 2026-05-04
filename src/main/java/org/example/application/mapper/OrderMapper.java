package org.example.application.mapper;

import org.example.application.dto.order.OrderResponseDto;
import org.example.domain.order.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {
    public OrderResponseDto toDto(Order order) {
        return new OrderResponseDto(
                order.getId().value(),
                order.getType().name(),
                order.getCurrentState().name(),
                order.getTotalPrice().getAmountInCents(),
                order.getClientId().value(),
                order.getManagerId().value(),
                order.getCar().getColor().toString(),
                order.getCreatedAt(),
                order.getConfirmedAt(),
                order.getPaidAt(),
                order.getCompletedAt(),
                order.getCancelledAt(),
                order.getCar().getId().value()
        );
    }
}