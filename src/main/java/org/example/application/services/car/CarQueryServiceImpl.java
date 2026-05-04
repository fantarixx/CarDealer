package org.example.application.services.car;

import lombok.RequiredArgsConstructor;
import org.example.application.dto.car.CarSearchRequestDto;
import org.example.application.dto.car.CarSearchResponseDto;
import org.example.application.mapper.CarMapper;
import org.example.application.ports.CarQueryService;
import org.example.domain.car.Car;
import org.example.domain.car.CarId;
import org.example.domain.common.exception.EntityNotFoundException;
import org.example.domain.ports.CarRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarQueryServiceImpl implements CarQueryService {
    private final CarRepository carRepository;
    private final CarMapper carMapper;

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("isAuthenticated()")
    public List<CarSearchResponseDto> searchCars(CarSearchRequestDto request) {
        List<Car> allCars = carRepository.findAll();

        return allCars.stream()
                .filter(car -> matchesBrand(car, request.brand()))
                .filter(car -> matchesModel(car, request.model()))
                .filter(car -> matchesPrice(car, request.minPrice(), request.maxPrice()))
                .filter(car -> matchesYear(car, request.minYear(), request.maxYear()))
                .filter(car -> availableOnly(car, request.availableOnly()))
                .map(carMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("isAuthenticated()")
    public CarSearchResponseDto getCarDetails(CarId carId) {
        Car car = carRepository.findById(carId);
        if (car == null) {
            throw new EntityNotFoundException("Car with id " + carId.value() + " not found");
        }
        return carMapper.toDto(car);
    }

    private boolean matchesBrand(Car car, String brand) {
        return brand == null || brand.isBlank() || car.getModel().brand().equalsIgnoreCase(brand);
    }

    private boolean matchesModel(Car car, String model) {
        return model == null || model.isBlank() || car.getModel().name().equalsIgnoreCase(model);
    }

    private boolean matchesPrice(Car car, Long minPrice, Long maxPrice) {
        long price = car.totalPrice().getAmountInCents();
        if (minPrice != null && price < minPrice) {
            return false;
        }
        return maxPrice == null || price <= maxPrice;
    }

    private boolean matchesYear(Car car, Integer minYear, Integer maxYear) {
        int year = car.getModel().year().getValue();
        if (minYear != null && year < minYear) {
            return false;
        }
        return maxYear == null || year <= maxYear;
    }

    private boolean availableOnly(Car car, Boolean availableOnly) {
        return availableOnly == null || !availableOnly || car.isAvailable();
    }
}
