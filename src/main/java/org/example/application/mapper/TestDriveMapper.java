package org.example.application.mapper;

import org.example.application.dto.order.CarForTestDriveResponseDto;
import org.example.application.dto.order.TestDriveResponseDto;
import org.example.domain.car.Car;
import org.example.domain.order.TestDrive;
import org.springframework.stereotype.Component;

@Component
public class TestDriveMapper {

    public TestDriveResponseDto toDto(TestDrive testDrive) {
        return new TestDriveResponseDto(
                testDrive.getId().value(),
                testDrive.getClientId().value(),
                testDrive.getCarId().value(),
                testDrive.getScheduledAt(),
                testDrive.getCurrentState().name()
        );
    }

    public CarForTestDriveResponseDto toCarDto(Car car) {
        return new CarForTestDriveResponseDto(
                car.getId().value(),
                car.getModel().brand(),
                car.getModel().name(),
                car.getModel().brand() + " " + car.getModel().name()
        );
    }
}