package org.example.infrastructure.repository.memory;

import org.example.domain.car.Model;
import org.example.domain.car.ModelId;
import org.example.domain.common.exception.EntityNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ModelRepository implements org.example.domain.ports.ModelRepository {
    private static final Map<ModelId, Model> models = new ConcurrentHashMap<>();

    @Override
    public List<Model> findAll() {
        return new ArrayList<>(models.values());
    }

    @Override
    public Model findById(ModelId modelId) {
        return models.get(modelId);
    }

    @Override
    public Model findByName(String modelName) {
        return models.values().stream().filter(c -> c.name().equals(modelName)).findFirst().orElse(null);
    }

    @Override
    public Model save(Model model) {
        models.put(model.id(), model);
        return model;
    }

    @Override
    public Model update(Model model) {
        ModelId newId = model.id();

        if (!models.containsKey(newId)) {
            throw new EntityNotFoundException("Model " + model.name() + " not found.");
        }

        models.put(newId, model);
        return model;
    }

    @Override
    public List<Model> findWithFilters(String brand, Set<UUID> componentIds) {
        //developing
        return findAll();
    }

    @Override
    public void delete(ModelId modelId) {
        if (!models.containsKey(modelId)) {
            throw new EntityNotFoundException("model with id: " + modelId.value() + " not found.");
        }

        models.remove(modelId);
    }
}

