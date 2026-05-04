package org.example.application.ports;

import org.example.application.dto.car.CarSearchRequestDto;
import org.example.application.dto.car.CarSearchResponseDto;
import org.example.domain.car.CarId;

import java.util.List;

public interface CarQueryService {
    List<CarSearchResponseDto> searchCars(CarSearchRequestDto request);
    CarSearchResponseDto getCarDetails(CarId carId);
}
