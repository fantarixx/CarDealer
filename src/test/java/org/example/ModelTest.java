package org.example;

import org.example.domain.car.*;
import org.example.domain.common.Money;
import org.example.domain.common.exception.DomainValidationException;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

class ModelTest {

    private final ModelId id = new ModelId();
    private final Component standardWheel = new Component(
            new ComponentId(),
            ComponentType.WHEELS,
            "17'' Standard",
            Money.zero(),
            Set.of()
    );
    private final Map<ComponentType, Component> standardComponents = Map.of(ComponentType.WHEELS, standardWheel);

    @Test
    void shouldCreateValidCarModel() {
        Model model = new Model(
                id,
                "320i", "BMW",
                Money.of(3_000_000),
                EngineType.GASOLINE, Year.of(2023),
                BodyType.SEDAN, DriveType.RWD, TransmissionType.AUTOMATIC_8,
                5,
                standardComponents
        );

        assertThat(model.name()).isEqualTo("320i");
        assertThat(model.brand()).isEqualTo("BMW");
        assertThat(model.basePrice().amount()).isEqualByComparingTo("3000000");
        assertThat(model.engineType()).isEqualTo(EngineType.GASOLINE);
        assertThat(model.year()).isEqualTo(Year.of(2023));
        assertThat(model.bodyType()).isEqualTo(BodyType.SEDAN);
        assertThat(model.driveType()).isEqualTo(DriveType.RWD);
        assertThat(model.transmission()).isEqualTo(TransmissionType.AUTOMATIC_8);
        assertThat(model.seatsCount()).isEqualTo(5);
        assertThat(model.standardComponents()).containsKey(ComponentType.WHEELS);
    }

    @Test
    void shouldThrowWhenNameIsNull() {
        assertThatThrownBy(() -> new Model(
                id, null, "BMW", Money.of(3_000_000),
                EngineType.GASOLINE, Year.of(2023), BodyType.SEDAN, DriveType.RWD,
                TransmissionType.AUTOMATIC_8, 5, standardComponents
        )).isInstanceOf(DomainValidationException.class)
                .hasMessage("Model name required.");
    }

    @Test
    void shouldThrowWhenNameIsBlank() {
        assertThatThrownBy(() -> new Model(
                id, "   ", "BMW", Money.of(3_000_000),
                EngineType.GASOLINE, Year.of(2023), BodyType.SEDAN, DriveType.RWD,
                TransmissionType.AUTOMATIC_8, 5, standardComponents
        )).isInstanceOf(DomainValidationException.class)
                .hasMessage("Model name can't be empty.");
    }

    @Test
    void shouldThrowWhenBrandIsNull() {
        assertThatThrownBy(() -> new Model(
                id, "320i", null, Money.of(3_000_000),
                EngineType.GASOLINE, Year.of(2023), BodyType.SEDAN, DriveType.RWD,
                TransmissionType.AUTOMATIC_8, 5, standardComponents
        )).isInstanceOf(DomainValidationException.class)
                .hasMessage("Model brand required.");
    }

    @Test
    void shouldThrowWhenBasePriceIsNull() {
        assertThatThrownBy(() -> new Model(
                id, "320i", "BMW", null,
                EngineType.GASOLINE, Year.of(2023), BodyType.SEDAN, DriveType.RWD,
                TransmissionType.AUTOMATIC_8, 5, standardComponents
        )).isInstanceOf(DomainValidationException.class)
                .hasMessage("Model base price required.");
    }

    @Test
    void shouldThrowWhenBasePriceIsZeroOrNegative() {
        assertThatThrownBy(() -> new Model(
                id, "320i", "BMW", Money.zero(),
                EngineType.GASOLINE, Year.of(2023), BodyType.SEDAN, DriveType.RWD,
                TransmissionType.AUTOMATIC_8, 5, standardComponents
        )).isInstanceOf(DomainValidationException.class)
                .hasMessage("Model base price must be positive.");
    }

    @Test
    void shouldThrowWhenYearIsOutOfRange() {
        assertThatThrownBy(() -> new Model(
                id, "320i", "BMW", Money.of(3_000_000),
                EngineType.GASOLINE, Year.of(1000), BodyType.SEDAN, DriveType.RWD,
                TransmissionType.AUTOMATIC_8, 5, standardComponents
        )).isInstanceOf(DomainValidationException.class)
                .hasMessageStartingWith("Model year must be 1500-");
    }

    @Test
    void shouldThrowWhenSeatsCountInvalid() {
        assertThatThrownBy(() -> new Model(
                id, "320i", "BMW", Money.of(3_000_000),
                EngineType.GASOLINE, Year.of(2023), BodyType.SEDAN, DriveType.RWD,
                TransmissionType.AUTOMATIC_8, 1, standardComponents
        )).isInstanceOf(DomainValidationException.class)
                .hasMessage("Seats count must be 2-7.");
    }

    @Test
    void shouldThrowWhenStandardComponentsIsEmpty() {
        assertThatThrownBy(() -> new Model(
                id, "320i", "BMW", Money.of(3_000_000),
                EngineType.GASOLINE, Year.of(2023), BodyType.SEDAN, DriveType.RWD,
                TransmissionType.AUTOMATIC_8, 5, Map.of()
        )).isInstanceOf(DomainValidationException.class)
                .hasMessage("Standard components can't be empty");
    }
}
