package org.example.application.services.configuration;

import lombok.RequiredArgsConstructor;
import org.example.application.dto.configuration.CarConfigureRequestDto;
import org.example.application.dto.configuration.ComponentResponseDto;
import org.example.application.dto.configuration.CarConfigurationResponseDto;
import org.example.application.mapper.ConfigurationMapper;
import org.example.application.ports.ConfigurationService;
import org.example.domain.car.Model;
import org.example.domain.car.Component;
import org.example.domain.car.ComponentId;
import org.example.domain.car.ComponentType;
import org.example.domain.common.exception.DomainValidationException;
import org.example.domain.common.exception.IncompatibleComponentException;
import org.example.domain.common.exception.EntityNotFoundException;
import org.example.domain.ports.ModelRepository;
import org.example.domain.ports.ComponentRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConfigurationServiceImpl implements ConfigurationService {
    private final ComponentRepository componentRepository;
    private final ModelRepository modelRepository;
    private final ConfigurationMapper mapper;

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("isAuthenticated()")
    public CarConfigurationResponseDto configureCar(CarConfigureRequestDto request) {
        Model model = modelRepository.findByName(request.modelName());
        if (model == null) {
            throw new EntityNotFoundException("Model not found: " + request.modelName());
        }

        List<Component> selectedComponents = new ArrayList<>();
        Map<ComponentType, Component> selectedByType = new HashMap<>();

        for (UUID componentId : request.componentIds()) {
            Component component = componentRepository.findById(new ComponentId(componentId));
            if (component == null) {
                throw new EntityNotFoundException("Component not found: " + componentId);
            }
            selectedComponents.add(component);
            if (selectedByType.containsKey(component.type())) {
                throw new DomainValidationException("Multiple components of type " + component.type() + " selected");
            }
            selectedByType.put(component.type(), component);
        }

        Set<ComponentType> requiredTypes = model.standardComponents().keySet();
        for (ComponentType required : requiredTypes) {
            if (!selectedByType.containsKey(required)) {
                throw new DomainValidationException("Missing component for required type: " + required);
            }
        }

        for (Component component : selectedComponents) {
            if (!component.compatibleModels().contains(model.id())) {
                throw new IncompatibleComponentException(
                        "Component " + component.name() + " is not compatible with model " + model.name()
                );
            }
        }

        long totalPrice = model.basePrice().getAmountInCents();
        for (Component component : selectedComponents) {
            totalPrice += component.price().getAmountInCents();
        }

        return mapper.toResponse(model, selectedComponents, totalPrice, "VALID");
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("isAuthenticated()")
    public List<ComponentResponseDto> getCompatibleComponents(String modelName) {
        Model model = modelRepository.findByName(modelName);
        if (model == null) {
            throw new EntityNotFoundException("Model not found: " + modelName);
        }
        List<Component> compatible = componentRepository.findCompatible(model);
        return compatible.stream().map(m -> mapper.toComponentResponse(m, modelRepository)).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("isAuthenticated()")
    public long calculateTotalPrice(Model model, List<Component> components) {
        Set<ComponentType> requiredTypes = model.standardComponents().keySet();
        Set<ComponentType> providedTypes = components.stream().map(Component::type).collect(Collectors.toSet());
        for (ComponentType required : requiredTypes) {
            if (!providedTypes.contains(required)) {
                throw new DomainValidationException("Missing component for type: " + required);
            }
        }
        for (Component comp : components) {
            if (!comp.compatibleModels().contains(model.id())) {
                throw new IncompatibleComponentException("Component " + comp.name() +
                        " incompatible with " + model.name());
            }
        }

        long total = model.basePrice().getAmountInCents();
        total += components.stream().mapToLong(c -> c.price().getAmountInCents()).sum();
        return total;
    }
}





