package org.example.infrastructure.repository.memory;

import org.example.domain.car.Component;
import org.example.domain.car.ComponentId;
import org.example.domain.car.Model;
import org.example.domain.car.ModelId;
import org.example.domain.common.exception.EntityNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ComponentRepository implements org.example.domain.ports.ComponentRepository {
    private static final Map<ComponentId, Component> components = new ConcurrentHashMap<>();

    @Override
    public List<Component> findAll() {
        return new ArrayList<>(components.values());
    }

    @Override
    public Component findById(ComponentId componentId) {
        return components.get(componentId);
    }

    @Override
    public Component findByName(String name) {
        return components.values().stream().filter(c -> c.name().equals(name)).findFirst().orElse(null);
    }

    @Override
    public List<Component> findCompatible(Model model) {
        return components.values().stream()
                .filter(Objects::nonNull).filter(c -> c.compatibleModels() != null)
                .filter(c -> c.compatibleModels().contains(model.id())).toList();
    }

    @Override
    public Component save(Component component) {
        components.put(component.id(), component);
        return component;
    }

    @Override
    public Component update(Component component) {
        ComponentId newId = component.id();

        if (!components.containsKey(newId)) {
            throw new EntityNotFoundException("Component " + component.name() + " not found.");
        }

        components.put(newId, component);
        return component;
    }

    @Override
    public void delete(ComponentId componentId) {
        if (!components.containsKey(componentId)) {
            throw new EntityNotFoundException("Component with id: " + componentId + " not found.");
        }

        components.remove(componentId);
    }

    @Override
    public void addCompatibility(ComponentId componentId, Model model) {
        Component component = components.get(componentId);
        if (component == null) {
            throw new EntityNotFoundException("Component with id: " + componentId.value() + " not found.");
        }

        component.compatibleModels().add(model.id());
    }
    @Override
    public void removeCompatibility(ComponentId componentId, Model model) {
        Component component = components.get(componentId);
        if (component == null) {
            throw new EntityNotFoundException("Component with id: " + componentId.value() + " not found.");
        }
        Set<ModelId> models = component.compatibleModels();
        if (models == null || !models.contains(model.id())) {
            throw new EntityNotFoundException(
                    "Component with id: " + componentId.value() +
                    " does not have compatibility with model " + model.name());
        }
        models.remove(model.id());
    }
}
