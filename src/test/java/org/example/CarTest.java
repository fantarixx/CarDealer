package org.example;

import org.example.domain.car.*;
import org.example.domain.common.Color;
import org.example.domain.common.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

class CarTest {

    private Model model;
    private Component standardWheel;
    private Component optionalWheel;
    private Color color;

    @BeforeEach
    void setUp() {
        standardWheel = new Component(
                new ComponentId(),
                ComponentType.WHEELS,
                "17'' Standard",
                Money.zero(),
                Set.of()
        );
        optionalWheel = new Component(
                new ComponentId(),
                ComponentType.WHEELS,
                "19'' M-Sport",
                Money.of(95_000),
                Set.of()
        );
        Map<ComponentType, Component> standardComponents = Map.of(ComponentType.WHEELS, standardWheel);
        model = new Model(
                new ModelId(),
                "320i", "BMW",
                Money.of(3_000_000),
                EngineType.GASOLINE, Year.of(2023),
                BodyType.SEDAN, DriveType.RWD, TransmissionType.AUTOMATIC_8,
                5,
                standardComponents
        );
        color = new Color(255, 255, 255);
    }

    @Test
    void shouldCreateCarWithStandardComponents() {
        Car car = new Car(new CarId(), model, color);
        assertThat(car.getId()).isNotNull();
        assertThat(car.getModel()).isEqualTo(model);
        assertThat(car.isAvailable()).isTrue();
        assertThat(car.getComponents()).containsKey(ComponentType.WHEELS);
        assertThat(car.getComponents().get(ComponentType.WHEELS)).isEqualTo(standardWheel);
        assertThat(car.totalPrice().amount()).isEqualByComparingTo("3000000");
    }

    @Test
    void shouldAddComponent() {
        Car car = new Car(new CarId(), model, color);
        car.addComponent(optionalWheel);
        assertThat(car.getComponents().get(ComponentType.WHEELS)).isEqualTo(optionalWheel);
        assertThat(car.totalPrice().amount()).isEqualByComparingTo("3095000"); // base 3M + 95k
    }

    @Test
    void shouldReserveAndRelease() {
        Car car = new Car(new CarId(), model, color);
        assertThat(car.isAvailable()).isTrue();

        car.setAvailable(false);
        assertThat(car.isAvailable()).isFalse();

        car.setAvailable(true);
        assertThat(car.isAvailable()).isTrue();
    }

    @Test
    void totalPriceShouldSumBasePriceAndComponentPrices() {
        Car car = new Car(new CarId(), model, color);
        car.addComponent(optionalWheel);
        Money expected = model.basePrice().add(optionalWheel.price());
        assertThat(car.totalPrice().amount()).isEqualByComparingTo(expected.amount());
    }
}
