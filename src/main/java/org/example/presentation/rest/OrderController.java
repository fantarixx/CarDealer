package org.example.presentation.rest;

import org.example.application.dto.order.OrderCreateBasicRequestDto;
import org.example.application.dto.order.OrderCreateSpecificRequestDto;
import org.example.application.dto.order.OrderResponseDto;
import org.example.application.ports.OrderCommandService;
import org.example.application.ports.OrderQueryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderCommandService orderCommandService;
    private final OrderQueryService orderQueryService;

    public OrderController(OrderCommandService orderCommandService, OrderQueryService orderQueryService) {
        this.orderCommandService = orderCommandService;
        this.orderQueryService = orderQueryService;
    }

    @PostMapping("/basic")
    public OrderResponseDto createBasicOrder(@RequestBody OrderCreateBasicRequestDto request) {
        return orderCommandService.createBasicOrder(request);
    }

    @PostMapping("/specific")
    public OrderResponseDto createSpecificOrder(@RequestBody OrderCreateSpecificRequestDto request) {
        return orderCommandService.createSpecificOrder(request);
    }

    @PostMapping("/{id}/confirm")
    public OrderResponseDto confirmOrder(@PathVariable UUID id) {
        return orderCommandService.confirmOrder(id);
    }

    @PostMapping("/{id}/pay")
    public OrderResponseDto payOrder(@PathVariable UUID id) {
        return orderCommandService.payOrder(id);
    }

    @PostMapping("/{id}/ready")
    public OrderResponseDto markReady(@PathVariable UUID id) {
        return orderCommandService.markReady(id);
    }

    @PostMapping("/{id}/complete")
    public OrderResponseDto completeOrder(@PathVariable UUID id) {
        return orderCommandService.completeOrder(id);
    }

    @PostMapping("/{id}/cancel")
    public OrderResponseDto cancelOrder(@PathVariable UUID id) {
        return orderCommandService.cancelOrder(id);
    }

    @GetMapping("/{id}")
    public OrderResponseDto getOrder(@PathVariable UUID id) {
        return orderQueryService.getOrderById(id);
    }

    @GetMapping("/client/{clientId}")
    public List<OrderResponseDto> getOrdersByClient(@PathVariable UUID clientId) {
        return orderQueryService.getOrdersByClient(clientId);
    }

    @GetMapping
    public List<OrderResponseDto> getAllOrders() {
        return orderQueryService.getAllOrders();
    }
}
