package org.example.application.services.car;

import lombok.RequiredArgsConstructor;
import org.example.application.dto.car.CarCommandDto;
import org.example.application.dto.car.CarSearchResponseDto;
import org.example.application.mapper.CarMapper;
import org.example.application.ports.CarCommandService;
import org.example.domain.car.Car;
import org.example.domain.car.CarId;
import org.example.domain.car.Model;
import org.example.domain.common.Color;
import org.example.domain.common.exception.DomainValidationException;
import org.example.domain.common.exception.EntityNotFoundException;
import org.example.domain.ports.ModelRepository;
import org.example.domain.ports.CarRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CarCommandServiceImpl implements CarCommandService {
    private final CarRepository carRepository;
    private final ModelRepository modelRepository;
    private final CarMapper carMapper;

    @Override
    @Transactional()
    @PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE_ADMIN')")
    public CarSearchResponseDto updateAvailability(CarId carId, boolean available) {
        Car car = carRepository.findById(carId);
        if (car == null) {
            throw new EntityNotFoundException("Car not found: " + carId.value());
        }
        car.setAvailable(available);
        return carMapper.toDto(carRepository.save(car));
    }

    @Override
    @Transactional()
    @PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE_ADMIN')")
    public CarId createCar(CarCommandDto request) {
        if (request.modelName() == null || request.modelName().trim().isEmpty()) {
            throw new DomainValidationException("Model name is required");
        }
        Model model = modelRepository.findByName(request.modelName());
        if (model == null) {
            throw new EntityNotFoundException("Model not found: " + request.modelName());
        }
        Car car = new Car(new CarId(), model, new Color(request.color()));
        car.setAvailable(request.initiallyAvailable() != null && request.initiallyAvailable());
        return carRepository.save(car).getId();
    }

    @Transactional()
    @PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE_ADMIN')")
    @Override
    public void deleteCar(CarId carId) {
        carRepository.delete(carId);
    }
}
