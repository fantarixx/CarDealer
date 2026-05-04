package org.example.domain.ports;

import org.example.domain.car.Component;
import org.example.domain.car.ComponentId;
import org.example.domain.car.Model;

import java.util.List;

public interface ComponentRepository {
    List<Component> findAll();
    public Component findById(ComponentId componentId);
    Component findByName(String name);
    List<Component> findCompatible(Model model);
    Component save(Component component);
    Component update(Component component);
    void delete(ComponentId componentId);
    void addCompatibility(ComponentId componentId, Model model);
    void removeCompatibility(ComponentId componentId, Model model);
}

