package org.example.presentation.rest;

import org.example.application.dto.order.TestDriveScheduleRequestDto;
import org.example.application.dto.order.TestDriveResponseDto;
import org.example.application.dto.order.CarForTestDriveResponseDto;
import org.example.application.ports.TestDriveCommandService;
import org.example.application.ports.TestDriveQueryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/test-drives")
public class TestDriveController {

    private final TestDriveCommandService testDriveCommandService;
    private final TestDriveQueryService testDriveQueryService;

    public TestDriveController(TestDriveCommandService testDriveCommandService,
                               TestDriveQueryService testDriveQueryService) {
        this.testDriveCommandService = testDriveCommandService;
        this.testDriveQueryService = testDriveQueryService;
    }

    @PostMapping
    public TestDriveResponseDto scheduleTestDrive(@RequestBody TestDriveScheduleRequestDto request) {
        return testDriveCommandService.scheduleTestDrive(request);
    }

    @PostMapping("/{id}/cancel")
    public TestDriveResponseDto cancelTestDrive(@PathVariable UUID id) {
        return testDriveCommandService.cancelTestDrive(id);
    }

    @PostMapping("/{id}/complete")
    public TestDriveResponseDto completeTestDrive(@PathVariable UUID id) {
        return testDriveCommandService.completeTestDrive(id);
    }

    @PostMapping("/cars/{carId}")
    public void addCarToTestDriveList(@PathVariable UUID carId) {
        testDriveCommandService.addCarToTestDrive(carId);
    }

    @DeleteMapping("/cars/{carId}")
    public void removeCarFromTestDriveList(@PathVariable UUID carId) {
        testDriveCommandService.removeCarFromTestDrive(carId);
    }

    @GetMapping("/cars/available")
    public List<CarForTestDriveResponseDto> getAvailableCarsForTestDrive() {
        return testDriveQueryService.getCarsAvailableForTestDrive();
    }

    @GetMapping("/{id}")
    public TestDriveResponseDto getTestDrive(@PathVariable UUID id) {
        return testDriveQueryService.getTestDriveById(id);
    }

    @GetMapping("/car/{carId}")
    public List<TestDriveResponseDto> getTestDrivesByCar(@PathVariable UUID carId) {
        return testDriveQueryService.getTestDrivesByCar(carId);
    }

    @GetMapping("/client/{clientId}")
    public List<TestDriveResponseDto> getTestDrivesByClient(@PathVariable UUID clientId) {
        return testDriveQueryService.getTestDrivesByClient(clientId);
    }

    @GetMapping
    public List<TestDriveResponseDto> getAllTestDrives() {
        return testDriveQueryService.getAllTestDrives();
    }
}