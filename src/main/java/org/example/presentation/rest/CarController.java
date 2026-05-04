package org.example.presentation.rest;

import lombok.RequiredArgsConstructor;
import org.example.application.dto.car.CarCommandDto;
import org.example.application.dto.car.CarSearchRequestDto;
import org.example.application.dto.car.CarSearchResponseDto;
import org.example.application.ports.CarCommandService;
import org.example.application.ports.CarQueryService;
import org.example.domain.car.CarId;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cars")
public class CarController {
    private final CarQueryService carQueryService;
    private final CarCommandService carCommandService;

    @PostMapping("/search")
    public List<CarSearchResponseDto> searchCars(@RequestBody CarSearchRequestDto request) {
        return carQueryService.searchCars(request);
    }

    @GetMapping("/{id}")
    public CarSearchResponseDto getCar(@PathVariable UUID id) {
        return carQueryService.getCarDetails(new CarId(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UUID createCar(@RequestBody CarCommandDto request) {
        return carCommandService.createCar(request).value();
    }

    @PutMapping("/{id}/availability")
    public void updateAvailability(@PathVariable UUID id, @RequestParam boolean available) {
        carCommandService.updateAvailability(new CarId(id), available);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCar(@PathVariable UUID id) {
        carCommandService.deleteCar(new CarId(id));
    }
}