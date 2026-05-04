package org.example.infrastructure.repository.memory;

import org.example.domain.car.Car;
import org.example.domain.car.CarId;
import org.example.domain.common.exception.EntityNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class CarRepository implements org.example.domain.ports.CarRepository {
    private static final Map<CarId, Car> cars = new ConcurrentHashMap<>();

    @Override
    public List<Car> findAll() {
        return new ArrayList<>(cars.values());
    }

    @Override
    public Car findById(CarId carId) {
        return cars.get(carId);
    }

    @Override
    public Car save(Car car) {
        cars.put(car.getId(), car);
        return car;
    }

    @Override
    public Car update(Car car) {
        CarId newId = car.getId();

        if (!cars.containsKey(newId)) {
            throw new EntityNotFoundException("Car with id: " + car.getId().value() + " not found.");
        }

        cars.put(newId, car);
        return car;
    }

    @Override
    public void delete(CarId carId) {
        if (!cars.containsKey(carId)) {
            throw new EntityNotFoundException("Car with id: " + carId.value() + " not found.");
        }

        cars.remove(carId);
    }
}
