package org.example.domain.car;

import org.example.domain.common.Money;
import org.example.domain.common.exception.DomainValidationException;

import java.time.Year;
import java.util.Map;
import java.util.Objects;

public record Model(ModelId id, String name, String brand, Money basePrice, EngineType engineType, Year year,
                    BodyType bodyType, DriveType driveType, TransmissionType transmission, int seatsCount,
                    Map<ComponentType, Component> standardComponents) {
    public Model(ModelId id, String name, String brand, Money basePrice,
                 EngineType engineType, Year year, BodyType bodyType,
                 DriveType driveType, TransmissionType transmission, int seatsCount,
                 Map<ComponentType, Component> standardComponents) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.basePrice = basePrice;
        this.engineType = engineType;
        this.year = year;
        this.bodyType = bodyType;
        this.driveType = driveType;
        this.transmission = transmission;
        this.seatsCount = seatsCount;
        this.standardComponents = standardComponents;

        validate();
    }

    private void validate() {
        if (id == null) {
            throw new DomainValidationException("Model id required.");
        }
        if (name == null) {
            throw new DomainValidationException("Model name required.");
        }
        if (name.isBlank()) {
            throw new DomainValidationException("Model name can't be empty.");
        }
        if (brand == null) {
            throw new DomainValidationException("Model brand required.");
        }
        if (brand.isBlank()) {
            throw new DomainValidationException("Model brand can't be empty.");
        }
        if (basePrice == null) {
            throw new DomainValidationException("Model base price required.");
        }
        if (basePrice.isZeroOrNegative()) {
            throw new DomainValidationException("Model base price must be positive.");
        }
        if (engineType == null) {
            throw new DomainValidationException("Model engine type required.");
        }
        if (year == null) {
            throw new DomainValidationException("Model year required.");
        }
        if (year.isBefore(Year.of(1500)) || year.isAfter(Year.now())) {
            throw new DomainValidationException("Model year must be 1500-" + Year.now() + '.');
        }
        if (bodyType == null) {
            throw new DomainValidationException("Model body type required.");
        }
        if (driveType == null) {
            throw new DomainValidationException("Model drive type required.");
        }
        if (transmission == null) {
            throw new DomainValidationException("Model transmission required.");
        }
        if (seatsCount < 2 || seatsCount > 7) {
            throw new DomainValidationException("Seats count must be 2-7.");
        }
        if (standardComponents == null) {
            throw new DomainValidationException("Model standard components required.");
        }
        if (standardComponents.isEmpty()) {
            throw new DomainValidationException("Standard components can't be empty");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Model model = (Model) o;
        return Objects.equals(id, model.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}