package org.example.application.services.component;

import lombok.RequiredArgsConstructor;
import org.example.application.dto.component.ComponentCreateRequestDto;
import org.example.application.dto.component.ComponentUpdateRequestDto;
import org.example.application.dto.component.ComponentResponseDto;
import org.example.application.mapper.ComponentMapper;
import org.example.application.ports.ComponentCommandService;
import org.example.application.ports.ComponentQueryService;
import org.example.domain.car.Model;
import org.example.domain.car.ModelId;
import org.example.domain.car.Component;
import org.example.domain.car.ComponentId;
import org.example.domain.car.ComponentType;
import org.example.domain.common.Money;
import org.example.domain.common.exception.DomainValidationException;
import org.example.domain.common.exception.EntityNotFoundException;
import org.example.domain.ports.ModelRepository;
import org.example.domain.ports.ComponentRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ComponentServiceImpl implements ComponentCommandService, ComponentQueryService {
    private final ComponentRepository componentRepository;
    private final ModelRepository modelRepository;
    private final ComponentMapper mapper;

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'ADMIN')")
    public ComponentResponseDto createComponent(ComponentCreateRequestDto request) {
        ComponentType type;
        try {
            type = ComponentType.valueOf(request.type().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new DomainValidationException("Invalid component type: " + request.type());
        }

        if (request.price() < 0) {
            throw new DomainValidationException("Price cannot be negative");
        }
        Money price = Money.of(request.price());

        List<Model> models = request.compatibleModelIds().stream()
                .map(id -> {
                    Model model = modelRepository.findById(new ModelId(id));
                    if (model == null) {
                        throw new EntityNotFoundException("Model not found: " + id);
                    }
                    return model;
                })
                .toList();

        Component component = new Component(
                new ComponentId(),
                type,
                request.name(),
                price,
                models.stream().map(Model::id).collect(Collectors.toCollection(HashSet::new))
        );

        component = componentRepository.save(component);
        return mapper.toDto(component, modelRepository);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'ADMIN')")
    public ComponentResponseDto updateComponent(ComponentUpdateRequestDto request) {
        ComponentId id = new ComponentId(request.id());
        Component existing = componentRepository.findById(id);
        if (existing == null) {
            throw new EntityNotFoundException("Component not found: " + request.id());
        }

        String newName = request.name() != null ? request.name() : existing.name();
        Money newPrice = request.price() != null
                ? Money.of(request.price())
                : existing.price();

        Component updated = new Component(
                existing.id(),
                existing.type(),
                newName,
                newPrice,
                existing.compatibleModels()
        );

        updated = componentRepository.save(updated);
        return mapper.toDto(updated, modelRepository);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'ADMIN')")
    public void deleteComponent(UUID componentId) {
        ComponentId id = new ComponentId(componentId);
        if (componentRepository.findById(id) == null) {
            throw new EntityNotFoundException("Component not found: " + componentId);
        }
        componentRepository.delete(id);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'ADMIN')")
    public void addCompatibility(UUID componentId, UUID modelId) {
        ComponentId compId = new ComponentId(componentId);
        ModelId modelIdObj = new ModelId(modelId);

        Model model = modelRepository.findById(modelIdObj);
        if (model == null) {
            throw new EntityNotFoundException("Model not found: " + modelId);
        }

        componentRepository.addCompatibility(compId, model);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('WAREHOUSE_ADMIN', 'ADMIN')")
    public void removeCompatibility(UUID componentId, UUID modelId) {
        ComponentId compId = new ComponentId(componentId);
        ModelId modelIdObj = new ModelId(modelId);

        Model model = modelRepository.findById(modelIdObj);
        if (model == null) {
            throw new EntityNotFoundException("Model not found: " + modelId);
        }

        componentRepository.removeCompatibility(compId, model);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("isAuthenticated()")
    public ComponentResponseDto getComponentById(UUID componentId) {
        Component component = componentRepository.findById(new ComponentId(componentId));
        if (component == null) {
            throw new EntityNotFoundException("Component not found: " + componentId);
        }
        return mapper.toDto(component, modelRepository);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("isAuthenticated()")
    public List<ComponentResponseDto> getAllComponents() {
        return componentRepository.findAll().stream()
                .map(m -> mapper.toDto(m, modelRepository))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("isAuthenticated()")
    public List<ComponentResponseDto> getComponentsByType(String type) {
        ComponentType componentType;
        try {
            componentType = ComponentType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new DomainValidationException("Invalid component type: " + type);
        }
        return componentRepository.findAll().stream()
                .filter(c -> c.type() == componentType)
                .map(m -> mapper.toDto(m, modelRepository))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("isAuthenticated()")
    public List<ComponentResponseDto> getCompatibleComponentsForModel(UUID modelId) {
        Model model = modelRepository.findById(new ModelId(modelId));
        if (model == null) {
            throw new EntityNotFoundException("Model not found: " + modelId);
        }
        List<Component> compatible = componentRepository.findCompatible(model);
        return compatible.stream()
                .map(m -> mapper.toDto(m, modelRepository))
                .collect(Collectors.toList());
    }
}