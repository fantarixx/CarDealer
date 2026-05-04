package org.example.domain.order;

import lombok.Getter;
import lombok.Setter;
import org.example.domain.car.Car;
import org.example.domain.common.Color;
import org.example.domain.common.Money;
import org.example.domain.common.exception.DomainValidationException;
import org.example.domain.user.UserId;

import java.time.LocalDateTime;
import java.util.Objects;


@Getter
public final class Order {
    private final OrderId id;
    private final OrderType type;
    @Setter
    private OrderState currentState;
    private final UserId clientId;
    private final Car car;
    private final Money totalPrice;
    private final UserId managerId;
    @Setter
    private LocalDateTime createdAt;
    @Setter
    private LocalDateTime confirmedAt;
    @Setter
    private LocalDateTime paidAt;
    @Setter
    private LocalDateTime completedAt;
    @Setter
    private LocalDateTime cancelledAt;

    public Order(OrderId id, OrderType type, UserId managerId, Car car, UserId clientId) {
        this.id = Objects.requireNonNull(id, "Order id cannot be null.");
        this.type = Objects.requireNonNull(type, "Order type cannot be null.");
        currentState = OrderState.FORMED;
        this.car = Objects.requireNonNull(car, "Car in order cannot be null.");
        this.managerId = Objects.requireNonNull(managerId, "Manager id in order cannot be null.");
        this.clientId = Objects.requireNonNull(clientId, "Client id in order cannot be null.");
        totalPrice = Objects.requireNonNull(car.totalPrice(), "Car price in order cannot be null.");
        if (!car.isAvailable()) {
            throw new DomainValidationException("This vehicle is reserved.");
        }
        createdAt = LocalDateTime.now();
    }

    public void confirm() {
        if (currentState != OrderState.FORMED) {
            throw new DomainValidationException("Order must be FORMED to make it CONFIRMED.");
        }
        car.setAvailable(false);
        currentState = OrderState.CONFIRMED;
        confirmedAt = LocalDateTime.now();
    }

    public void payRequire() {
        if (currentState != OrderState.CONFIRMED) {
            throw new DomainValidationException("Order must be CONFIRMED to make it PENDING_PAYMENT.");
        }
        currentState = OrderState.PENDING_PAYMENT;
    }

    public void pay() {
        if (currentState != OrderState.PENDING_PAYMENT) {
            throw new DomainValidationException("Order must be PENDING_PAYMENT to make it PAID.");
        }
        currentState = OrderState.PAID;
        paidAt = LocalDateTime.now();
    }

    public void deliveryRequire() {
        if (type != OrderType.SPECIFIC_CONFIGURATION || currentState != OrderState.PAID) {
            throw new DomainValidationException("This order can't wait for delivery yet.");
        }
        currentState = OrderState.PENDING_DELIVERY;
    }

    public void ready() {
        if (!(type == OrderType.BASIC && currentState == OrderState.PAID ||
                type == OrderType.SPECIFIC_CONFIGURATION && currentState == OrderState.PENDING_DELIVERY)) {
            throw new DomainValidationException("This order can't be ready yet.");
        }
        currentState = OrderState.READY;
    }
    public void complete() {
        if (currentState != OrderState.READY) {
            throw new DomainValidationException("Order must be READY to make it COMPLETED.");
        }
        currentState = OrderState.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }
    public void cancel() {
        if (currentState != OrderState.FORMED && currentState != OrderState.CONFIRMED && currentState != OrderState.PENDING_PAYMENT) {
            throw new DomainValidationException("This order already can't be canceled.");
        }
        currentState = OrderState.CANCELLED;
        cancelledAt = LocalDateTime.now();
        car.setAvailable(true);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Order order && id.equals(order.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
