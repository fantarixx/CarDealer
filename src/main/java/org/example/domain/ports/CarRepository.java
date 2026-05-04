package org.example.domain.ports;

import org.example.domain.car.Car;
import org.example.domain.car.CarId;

import java.util.List;

public interface CarRepository {
    List<Car> findAll();
    Car findById(CarId carId);
    Car save(Car car);
    Car update(Car car);
    void delete(CarId carId);
}
