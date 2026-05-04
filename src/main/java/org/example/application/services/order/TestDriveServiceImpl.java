package org.example.application.services.order;

import lombok.RequiredArgsConstructor;
import org.example.application.dto.order.TestDriveScheduleRequestDto;
import org.example.application.dto.order.CarForTestDriveResponseDto;
import org.example.application.dto.order.TestDriveResponseDto;
import org.example.application.mapper.TestDriveMapper;
import org.example.application.ports.TestDriveCommandService;
import org.example.application.ports.TestDriveQueryService;
import org.example.domain.car.Car;
import org.example.domain.car.CarId;
import org.example.domain.common.exception.DomainValidationException;
import org.example.domain.common.exception.EntityNotFoundException;
import org.example.domain.order.TestDrive;
import org.example.domain.order.TestDriveId;
import org.example.domain.ports.CarRepository;
import org.example.domain.ports.TestDriveRepository;
import org.example.domain.user.UserId;
import org.example.infrastructure.security.SecurityUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestDriveServiceImpl implements TestDriveCommandService, TestDriveQueryService {
    private final TestDriveRepository testDriveRepository;
    private final CarRepository carRepository;
    private final TestDriveMapper mapper;
    private final SecurityUtils securityUtils;

    @Override
    @Transactional
    @PreAuthorize("hasRole('USER')")
    public TestDriveResponseDto scheduleTestDrive(TestDriveScheduleRequestDto request) {
        UserId clientId = new UserId(UUID.fromString(securityUtils.getCurrentUserId()));
        CarId carId = new CarId(request.carId());

        Car car = carRepository.findById(carId);
        if (car == null) {
            throw new EntityNotFoundException("Car not found: " + carId.value());
        }
        if (!car.isAvailableForTestDrive()) {
            throw new DomainValidationException("Car is not available for test drive");
        }
        if (!car.isAvailable()) {
            throw new DomainValidationException("Car is reserved or sold, cannot schedule test drive");
        }

        TestDrive testDrive = new TestDrive(new TestDriveId(), clientId, carId, request.scheduledAt());
        testDrive = testDriveRepository.save(testDrive);
        return mapper.toDto(testDrive);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('USER')")
    public TestDriveResponseDto cancelTestDrive(UUID testDriveId) {
        TestDrive testDrive = findTestDriveById(testDriveId);
        if (!testDrive.getClientId().equals(new UserId(UUID.fromString(securityUtils.getCurrentUserId())))) {
            throw new AccessDeniedException("You are not the owner of this order");
        }
        testDrive.cancel();
        testDrive = testDriveRepository.save(testDrive);
        return mapper.toDto(testDrive);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('MANAGER')")
    public TestDriveResponseDto completeTestDrive(UUID testDriveId) {
        TestDrive testDrive = findTestDriveById(testDriveId);
        testDrive.complete();
        testDrive = testDriveRepository.save(testDrive);
        return mapper.toDto(testDrive);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('WAREHOUSE_ADMIN')")
    public void addCarToTestDrive(UUID carId) {
        Car car = carRepository.findById(new CarId(carId));
        if (car == null) {
            throw new EntityNotFoundException("Car not found: " + carId);
        }
        if (!car.isAvailable()) {
            throw new DomainValidationException("Cannot add unavailable car to test drive list");
        }
        car.setAvailableForTestDrive(true);
        carRepository.save(car);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('WAREHOUSE_ADMIN')")
    public void removeCarFromTestDrive(UUID carId) {
        Car car = carRepository.findById(new CarId(carId));
        if (car == null) {
            throw new EntityNotFoundException("Car not found: " + carId);
        }
        car.setAvailableForTestDrive(false);
        carRepository.save(car);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("isAuthenticated()")
    public TestDriveResponseDto getTestDriveById(UUID testDriveId) {
        TestDrive testDrive = findTestDriveById(testDriveId);
        if (securityUtils.isRole("ROLE_USER") && !testDrive.getClientId().equals(new UserId(UUID.fromString(securityUtils.getCurrentUserId())))) {
            throw new AccessDeniedException("You are not the owner of this order");
        }
        return mapper.toDto(testDrive);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'MANAGER', 'ADMIN')")
    public List<TestDriveResponseDto> getTestDrivesByCar(UUID carId) {
        CarId carIdObj = new CarId(carId);
        return testDriveRepository.findAll().stream()
                .filter(td -> td.getCarId().equals(carIdObj))
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("isAuthenticated()")
    public List<TestDriveResponseDto> getTestDrivesByClient(UUID clientId) {
        UserId userId = new UserId(clientId);
        if (securityUtils.isRole("ROLE_USER") && !clientId.equals(UUID.fromString(securityUtils.getCurrentUserId()))) {
            throw new AccessDeniedException("You don't have permission to see this user test drives.");
        }
        return testDriveRepository.findAll().stream()
                .filter(td -> td.getClientId().equals(userId))
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public List<TestDriveResponseDto> getAllTestDrives() {
        return testDriveRepository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("isAuthenticated()")
    public List<CarForTestDriveResponseDto> getCarsAvailableForTestDrive() {
        return carRepository.findAll().stream()
                .filter(Car::isAvailableForTestDrive)
                .filter(Car::isAvailable).map(mapper::toCarDto)
                .collect(Collectors.toList());
    }

    private TestDrive findTestDriveById(UUID testDriveId) {
        TestDrive testDrive = testDriveRepository.findById(new TestDriveId(testDriveId));
        if (testDrive == null) {
            throw new EntityNotFoundException("Test drive not found: " + testDriveId);
        }
        return testDrive;
    }
}