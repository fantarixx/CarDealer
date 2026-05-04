package org.example.application.ports;

import org.example.application.dto.order.TestDriveScheduleRequestDto;
import org.example.application.dto.order.TestDriveResponseDto;

import java.util.UUID;

public interface TestDriveCommandService {
    TestDriveResponseDto scheduleTestDrive(TestDriveScheduleRequestDto request);
    TestDriveResponseDto cancelTestDrive(UUID testDriveId);
    TestDriveResponseDto completeTestDrive(UUID testDriveId);
    void addCarToTestDrive(UUID carId);
    void removeCarFromTestDrive(UUID carId);
}