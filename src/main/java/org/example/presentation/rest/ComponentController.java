package org.example.presentation.rest;

import lombok.RequiredArgsConstructor;
import org.example.application.dto.component.ComponentCreateRequestDto;
import org.example.application.dto.component.ComponentUpdateRequestDto;
import org.example.application.dto.component.ComponentResponseDto;
import org.example.application.ports.ComponentCommandService;
import org.example.application.ports.ComponentQueryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/components")
public class ComponentController {
    private final ComponentCommandService componentCommandService;
    private final ComponentQueryService componentQueryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ComponentResponseDto createComponent(@RequestBody ComponentCreateRequestDto request) {
        return componentCommandService.createComponent(request);
    }

    @PutMapping
    public ComponentResponseDto updateComponent(@RequestBody ComponentUpdateRequestDto request) {
        return componentCommandService.updateComponent(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComponent(@PathVariable UUID id) {
        componentCommandService.deleteComponent(id);
    }

    @PostMapping("/{componentId}/compatibility/{modelId}")
    public void addCompatibility(@PathVariable UUID componentId, @PathVariable UUID modelId) {
        componentCommandService.addCompatibility(componentId, modelId);
    }

    @DeleteMapping("/{componentId}/compatibility/{modelId}")
    public void removeCompatibility(@PathVariable UUID componentId, @PathVariable UUID modelId) {
        componentCommandService.removeCompatibility(componentId, modelId);
    }

    @GetMapping("/{id}")
    public ComponentResponseDto getComponent(@PathVariable UUID id) {
        return componentQueryService.getComponentById(id);
    }

    @GetMapping
    public List<ComponentResponseDto> getAllComponents() {
        return componentQueryService.getAllComponents();
    }

    @GetMapping("/type/{type}")
    public List<ComponentResponseDto> getComponentsByType(@PathVariable String type) {
        return componentQueryService.getComponentsByType(type);
    }

    @GetMapping("/model/{modelId}")
    public List<ComponentResponseDto> getCompatibleComponentsForModel(@PathVariable UUID modelId) {
        return componentQueryService.getCompatibleComponentsForModel(modelId);
    }
}
