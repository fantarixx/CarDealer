package org.example.presentation.rest;

import org.example.application.dto.configuration.CarConfigureRequestDto;
import org.example.application.dto.configuration.CarConfigurationResponseDto;
import org.example.application.dto.configuration.ComponentResponseDto;
import org.example.application.ports.ConfigurationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/configuration")
public class ConfigurationController {

    private final ConfigurationService configurationService;

    public ConfigurationController(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    @PostMapping("/configure")
    public CarConfigurationResponseDto configureCar(@RequestBody CarConfigureRequestDto request) {
        return configurationService.configureCar(request);
    }

    @GetMapping("/compatible/{modelName}")
    public List<ComponentResponseDto> getCompatibleComponents(@PathVariable String modelName) {
        return configurationService.getCompatibleComponents(modelName);
    }
}