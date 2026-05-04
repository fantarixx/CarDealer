package org.example.application.ports;

import org.example.application.dto.car.CarCommandDto;
import org.example.application.dto.car.CarSearchResponseDto;
import org.example.domain.car.CarId;

public interface CarCommandService {
    CarId createCar(CarCommandDto request);

    CarSearchResponseDto updateAvailability(CarId carId, boolean available);
    void deleteCar(CarId id);
}
