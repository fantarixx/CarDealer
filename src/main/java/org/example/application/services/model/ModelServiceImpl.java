package org.example.application.services.model;

import lombok.RequiredArgsConstructor;
import org.example.application.dto.model.CarModelCreateRequestDto;
import org.example.application.dto.model.CarModelUpdateRequestDto;
import org.example.application.dto.model.CarModelResponseDto;
import org.example.application.mapper.CarModelMapper;
import org.example.application.ports.ModelCommandService;
import org.example.application.ports.ModelQueryService;
import org.example.domain.car.*;
import org.example.domain.common.Money;
import org.example.domain.common.exception.DomainValidationException;
import org.example.domain.common.exception.EntityNotFoundException;
import org.example.domain.ports.ModelRepository;
import org.example.domain.ports.CarRepository;
import org.example.domain.ports.ComponentRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ModelServiceImpl implements ModelCommandService, ModelQueryService {
    private final ModelRepository modelRepository;
    private final CarRepository carRepository;
    private final ComponentRepository componentRepository;
    private final CarModelMapper mapper;

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'ADMIN')")
    public CarModelResponseDto createModel(CarModelCreateRequestDto request) {
        Model existing = modelRepository.findByName(request.name());
        if (existing != null) {
            throw new DomainValidationException("Model with name " + request.name() + " already exists");
        }

        Model model = mapper.toEntity(request, componentRepository);
        model = modelRepository.save(model);
        return mapper.toDto(model);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'ADMIN')")
    public CarModelResponseDto updateModel(CarModelUpdateRequestDto request) {
        ModelId id = new ModelId(request.id());
        Model existing = modelRepository.findById(id);
        if (existing == null) {
            throw new EntityNotFoundException("Model not found: " + request.id());
        }

        String name = request.name() != null ? request.name() : existing.name();
        String brand = request.brand() != null ? request.brand() : existing.brand();
        Money basePrice = request.basePrice() != null ? Money.of(request.basePrice()) :
                existing.basePrice();
        EngineType engineType = request.engineType() != null
                ? Enum.valueOf(EngineType.class, request.engineType().toUpperCase())
                : existing.engineType();
        Year year = request.year() != null ? Year.of(request.year()) : existing.year();
        BodyType bodyType = request.bodyType() != null
                ? Enum.valueOf(BodyType.class, request.bodyType().toUpperCase())
                : existing.bodyType();
        DriveType driveType = request.driveType() != null
                ? Enum.valueOf(DriveType.class, request.driveType().toUpperCase())
                : existing.driveType();
        TransmissionType transmission = request.transmission() != null
                ? Enum.valueOf(TransmissionType.class, request.transmission().toUpperCase())
                : existing.transmission();
        int seatsCount = request.seatsCount() != null ? request.seatsCount() : existing.seatsCount();
        Map<ComponentType, Component> standard = request.standardComponents() != null
                ? resolveStandardComponents(request.standardComponents())
                : existing.standardComponents();

        Model updated = new Model(
                existing.id(), name, brand, basePrice, engineType, year,
                bodyType, driveType, transmission, seatsCount, standard
        );

        updated = modelRepository.save(updated);
        return mapper.toDto(updated);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'ADMIN')")
    public void deleteModel(UUID modelId) {
        ModelId id = new ModelId(modelId);
        Model model = modelRepository.findById(id);
        if (model == null) {
            throw new EntityNotFoundException("Model not found: " + modelId);
        }

        boolean hasCars = carRepository.findAll().stream()
                .anyMatch(car -> car.getModel().id().equals(id));
        if (hasCars) {
            throw new DomainValidationException("Cannot delete model that has existing cars");
        }

        modelRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("isAuthenticated()")
    public CarModelResponseDto getModelById(UUID modelId) {
        Model model = modelRepository.findById(new ModelId(modelId));
        if (model == null) {
            throw new EntityNotFoundException("Model not found: " + modelId);
        }
        return mapper.toDto(model);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("isAuthenticated()")
    public List<CarModelResponseDto> getAllModels() {
        return modelRepository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("isAuthenticated()")
    public CarModelResponseDto getModelByName(String name) {
        Model model = modelRepository.findByName(name);
        if (model == null) {
            throw new EntityNotFoundException("Model not found: " + name);
        }
        return mapper.toDto(model);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("isAuthenticated()")
    public List<CarModelResponseDto> getModels(String brand, Set<UUID> componentIds) {
        List<Model> models = modelRepository.findWithFilters(brand, componentIds);
        return models.stream().map(mapper::toDto).collect(Collectors.toList());
    }

    private Map<ComponentType, Component> resolveStandardComponents(Map<String, UUID> standardMap) {
        Map<ComponentType, Component> result = new HashMap<>();
        for (Map.Entry<String, UUID> entry : standardMap.entrySet()) {
            ComponentType type = Enum.valueOf(ComponentType.class, entry.getKey().toUpperCase());
            Component component = componentRepository.findById(new ComponentId(entry.getValue()));
            if (component == null) {
                throw new DomainValidationException("Component not found: " + entry.getValue());
            }
            result.put(type, component);
        }
        return result;
    }
}