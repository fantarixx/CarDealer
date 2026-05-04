package org.example.presentation.rest;

import lombok.RequiredArgsConstructor;
import org.example.application.dto.model.CarModelCreateRequestDto;
import org.example.application.dto.model.CarModelUpdateRequestDto;
import org.example.application.dto.model.CarModelResponseDto;
import org.example.application.ports.ModelCommandService;
import org.example.application.ports.ModelQueryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/models")
public class ModelController {
    private final ModelCommandService carModelCommandService;
    private final ModelQueryService carModelQueryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CarModelResponseDto createModel(@RequestBody CarModelCreateRequestDto request) {
        return carModelCommandService.createModel(request);
    }

    @PutMapping
    public CarModelResponseDto updateModel(@RequestBody CarModelUpdateRequestDto request) {
        return carModelCommandService.updateModel(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteModel(@PathVariable UUID id) {
        carModelCommandService.deleteModel(id);
    }

    @GetMapping("/{id}")
    public CarModelResponseDto getModelById(@PathVariable UUID id) {
        return carModelQueryService.getModelById(id);
    }

    @GetMapping("/name/{name}")
    public CarModelResponseDto getModelByName(@PathVariable String name) {
        return carModelQueryService.getModelByName(name);
    }

    @GetMapping
    public List<CarModelResponseDto> getAllModels() {
        return carModelQueryService.getAllModels();
    }

    @GetMapping("/filter")
    public List<CarModelResponseDto> getModels(@RequestParam(required = false) String brand,
                                               @RequestParam(required = false) Set<UUID> componentIds) {
        return carModelQueryService.getModels(brand, componentIds);
    }
}