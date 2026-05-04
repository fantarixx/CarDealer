package org.example.domain.car;

import lombok.Getter;
import lombok.Setter;
import org.example.domain.common.Color;
import org.example.domain.common.Money;

import java.time.LocalDateTime;
import java.util.*;

@Getter
public class Car {
    private final CarId id;
    private final Model model;
    @Setter
    private Color color;
    @Setter
    private boolean isAvailable;
    @Setter
    private boolean availableForTestDrive;
    private final LocalDateTime createdAt;
    private final Map<ComponentType, Component> components;


    public Car(CarId id, Model model, Color color) {
        this.id = Objects.requireNonNull(id, "Car id cannot be null.");
        this.model = Objects.requireNonNull(model, "Car model cannot be null.");
        this.color = Objects.requireNonNull(color, "Car color cannot be null.");
        isAvailable = true;
        availableForTestDrive = true;
        components = new HashMap<>(model.standardComponents());
        this.createdAt = LocalDateTime.now();
    }

    public void addComponent(Component component) {
        components.put(component.type(), component);
    }

    public Money totalPrice() {
        Money componentsSum = components.values().stream()
                .map(Component::price)
                .reduce(Money.zero(), Money::add);
        return model.basePrice().add(componentsSum);
    }
}