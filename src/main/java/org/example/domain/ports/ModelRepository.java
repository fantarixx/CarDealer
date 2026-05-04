package org.example.domain.ports;

import org.example.domain.car.Model;
import org.example.domain.car.ModelId;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ModelRepository {
    List<Model> findAll();
    Model findById(ModelId modelId);
    Model findByName(String modelName);
    List<Model> findWithFilters(String brand, Set<UUID> componentIds);
    Model save(Model model);
    Model update(Model model);
    void delete(ModelId modelId);
}
