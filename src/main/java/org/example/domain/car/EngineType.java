package org.example.domain.car;

import lombok.Getter;

@Getter
public enum EngineType {
    GASOLINE("Gas", 2.0, (short) 290),
    DIESEL("Diesel", 2.0, (short) 600),
    HYBRID("Hybrid", 2.5, (short) 250),
    ELECTRIC("Electro", 0.0, (short) 200);

    private final String displayName;
    private final double displacement;
    private final short power;

    EngineType(String displayName, double displacement, short power) {
        this.displayName = displayName;
        this.displacement = displacement;
        this.power = power;
    }
}

