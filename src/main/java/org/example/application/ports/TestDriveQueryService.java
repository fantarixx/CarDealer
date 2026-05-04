package org.example.application.ports;

import org.example.application.dto.order.CarForTestDriveResponseDto;
import org.example.application.dto.order.TestDriveResponseDto;

import java.util.List;
import java.util.UUID;

public interface TestDriveQueryService {
    TestDriveResponseDto getTestDriveById(UUID testDriveId);
    List<TestDriveResponseDto> getTestDrivesByCar(UUID carId);
    List<TestDriveResponseDto> getTestDrivesByClient(UUID clientId);
    List<TestDriveResponseDto> getAllTestDrives();
    List<CarForTestDriveResponseDto> getCarsAvailableForTestDrive();
}