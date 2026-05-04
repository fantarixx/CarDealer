package org.example.application.services.order;

import lombok.RequiredArgsConstructor;
import org.example.application.dto.order.OrderCreateBasicRequestDto;
import org.example.application.dto.order.OrderCreateSpecificRequestDto;
import org.example.application.dto.order.OrderResponseDto;
import org.example.application.mapper.OrderMapper;
import org.example.application.ports.OrderCommandService;
import org.example.application.ports.OrderQueryService;
import org.example.domain.car.*;
import org.example.domain.common.Color;
import org.example.domain.common.exception.DomainValidationException;
import org.example.domain.common.exception.EntityNotFoundException;
import org.example.domain.order.Order;
import org.example.domain.order.OrderId;
import org.example.domain.order.OrderType;
import org.example.domain.ports.*;
import org.example.domain.user.User;
import org.example.domain.user.UserId;
import org.example.infrastructure.security.SecurityUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderCommandService, OrderQueryService {

    private final OrderRepository orderRepository;
    private final CarRepository carRepository;
    private final ModelRepository modelRepository;
    private final ComponentRepository componentRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    private final SecurityUtils securityUtils;

    private UserId getDefaultManager() {
        List<User> managers = userRepository.findAll().stream()
                .filter(u -> u.getRole().canManageTestOrders()).toList();
        if (managers.isEmpty()) {
            throw new DomainValidationException("No manager available to handle order");
        }
        return managers.getFirst().getId();
    }


    @Override
    @PreAuthorize("hasRole('USER')")
    public OrderResponseDto createBasicOrder(OrderCreateBasicRequestDto request) {
        UserId clientId = new UserId(UUID.fromString(securityUtils.getCurrentUserId()));
        CarId carId = new CarId(request.carId());

        Car car = carRepository.findById(carId);
        if (car == null) {
            throw new EntityNotFoundException("Car not found: " + carId.value());
        }
        if (!car.isAvailable()) {
            throw new DomainValidationException("Car is not available for purchase");
        }

        UserId managerId = getDefaultManager();

        Order order = new Order(new OrderId(), OrderType.BASIC, managerId, car, clientId);
        order = orderRepository.save(order);

        return orderMapper.toDto(order);
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public OrderResponseDto createSpecificOrder(OrderCreateSpecificRequestDto request) {
        UserId clientId = new UserId(UUID.fromString(securityUtils.getCurrentUserId()));

        Model model = modelRepository.findByName(request.modelName());
        if (model == null) {
            throw new EntityNotFoundException("Model not found: " + request.modelName());
        }

        List<Component> components = request.componentIds().stream()
                .map(id -> componentRepository.findById(new ComponentId(id)))
                .peek(comp -> {
                    if (comp == null) {
                        throw new EntityNotFoundException("Component for specific not found.");
                    }
                }).toList();


        Color defaultColor = new Color("WHITE");
        Car configuredCar = new Car(new CarId(), model, defaultColor);
        components.forEach(configuredCar::addComponent);
        configuredCar.setAvailable(false);

        configuredCar = carRepository.save(configuredCar);

        UserId managerId = getDefaultManager();
        Order order = new Order(new OrderId(), OrderType.SPECIFIC_CONFIGURATION, managerId, configuredCar, clientId);
        order = orderRepository.save(order);

        return orderMapper.toDto(order);
    }

    @Override
    @PreAuthorize("hasRole('WAREHOUSE_ADMIN')")
    public OrderResponseDto confirmOrder(UUID orderId) {
        Order order = findOrderById(orderId);

        if (order.getType() == OrderType.BASIC) {
            Car car = order.getCar();
            if (!car.isAvailable()) {
                throw new DomainValidationException("Car is already reserved");
            }
            car.setAvailable(false);
            carRepository.save(car);
        }

        order.confirm();
        order = orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Override
    @PreAuthorize("hasRole('MANAGER')")
    public OrderResponseDto payOrder(UUID orderId) {
        Order order = findOrderById(orderId);
        order.payRequire();
        order.pay();
        order = orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Override
    @PreAuthorize("hasRole('MANAGER')")
    public OrderResponseDto markReady(UUID orderId) {
        Order order = findOrderById(orderId);
        order.ready();
        order = orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Override
    @PreAuthorize("hasRole('MANAGER')")
    public OrderResponseDto completeOrder(UUID orderId) {
        Order order = findOrderById(orderId);
        order.complete();
        order = orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public OrderResponseDto cancelOrder(UUID orderId) {
        Order order = findOrderById(orderId);
        if (!order.getClientId().equals(new UserId(UUID.fromString(securityUtils.getCurrentUserId())))) {
            throw new AccessDeniedException("You are not the owner of this order");
        }
        order.cancel();

        Car car = order.getCar();
        if (car != null && !car.isAvailable()) {
            car.setAvailable(true);
            carRepository.save(car);
        }

        order = orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("isAuthenticated()")
    public OrderResponseDto getOrderById(UUID orderId) {
        Order order = findOrderById(orderId);
        if (securityUtils.isRole("ROLE_USER") &&
                !order.getClientId().equals(new UserId(UUID.fromString(securityUtils.getCurrentUserId())))) {
            throw new AccessDeniedException("You are not the owner of this order");
        }

        return orderMapper.toDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("isAuthenticated()")
    public List<OrderResponseDto> getOrdersByClient(UUID clientId) {
        UserId userId = new UserId(clientId);
        if (securityUtils.isRole("ROLE_USER") &&
                !clientId.equals(UUID.fromString(securityUtils.getCurrentUserId()))) {
            throw new AccessDeniedException("You don't have permission to see this user orders.");
        }

        return orderRepository.findAll().stream()
                .filter(o -> o.getClientId().equals(userId))
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public List<OrderResponseDto> getAllOrders() {
        return orderRepository.findAll().stream().map(orderMapper::toDto).toList();
    }

    private Order findOrderById(UUID orderId) {
        Order order = orderRepository.findById(new OrderId(orderId));
        if (order == null) {
            throw new EntityNotFoundException("Order not found: " + orderId);
        }
        return order;
    }
}