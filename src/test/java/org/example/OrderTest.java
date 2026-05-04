package org.example;

import org.example.domain.car.*;
import org.example.domain.common.Color;
import org.example.domain.common.Money;
import org.example.domain.common.exception.DomainValidationException;
import org.example.domain.order.Order;
import org.example.domain.order.OrderId;
import org.example.domain.order.OrderState;
import org.example.domain.order.OrderType;
import org.example.domain.user.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

class OrderTest {

    private Car car;
    private UserId clientId;
    private UserId managerId;

    @BeforeEach
    void setUp() {
        Model model = new Model(
                new ModelId(),
                "320i", "BMW",
                Money.of(3_000_000),
                EngineType.GASOLINE, Year.of(2023),
                BodyType.SEDAN, DriveType.RWD, TransmissionType.AUTOMATIC_8,
                5,
                Map.of(ComponentType.WHEELS,
                        new Component(new ComponentId(), ComponentType.WHEELS, "17'' Standard", Money.zero(), Set.of()))
        );
        car = new Car(new CarId(), model, new Color(255, 0, 10));
        clientId = new UserId();
        managerId = new UserId();
    }

    @Test
    void shouldCreateBasicOrder() {
        Order order = new Order(new OrderId(), OrderType.BASIC, managerId, car, clientId);
        assertThat(order.getId()).isNotNull();
        assertThat(order.getType()).isEqualTo(OrderType.BASIC);
        assertThat(order.getCurrentState()).isEqualTo(OrderState.FORMED);
        assertThat(car.isAvailable()).isTrue();
    }

    @Test
    void shouldThrowWhenCreatingOrderForUnavailableCar() {
        car.setAvailable(false);
        assertThatThrownBy(() -> new Order(new OrderId(), OrderType.BASIC, managerId, car, clientId))
                .isInstanceOf(DomainValidationException.class)
                .hasMessage("This vehicle is reserved.");
    }

    @Test
    void shouldTransitionThroughValidStates() {
        Order order = new Order(new OrderId(), OrderType.BASIC, managerId, car, clientId);

        order.confirm();
        assertThat(order.getCurrentState()).isEqualTo(OrderState.CONFIRMED);
        assertThat(car.isAvailable()).isFalse();

        order.payRequire();
        assertThat(order.getCurrentState()).isEqualTo(OrderState.PENDING_PAYMENT);

        order.pay();
        assertThat(order.getCurrentState()).isEqualTo(OrderState.PAID);

        order.ready();
        assertThat(order.getCurrentState()).isEqualTo(OrderState.READY);

        order.complete();
        assertThat(order.getCurrentState()).isEqualTo(OrderState.COMPLETED);
    }

    @Test
    void shouldCancelOrderBeforeConfirmation() {
        Order order = new Order(new OrderId(), OrderType.BASIC, managerId, car, clientId);
        order.cancel();
        assertThat(order.getCurrentState()).isEqualTo(OrderState.CANCELLED);
        assertThat(car.isAvailable()).isTrue();
    }

    @Test
    void shouldThrowWhenInvalidTransition() {
        Order order = new Order(new OrderId(), OrderType.BASIC, managerId, car, clientId);
        assertThatThrownBy(order::pay).isInstanceOf(DomainValidationException.class);
    }
}