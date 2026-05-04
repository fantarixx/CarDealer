package org.example.infrastructure.repository.jpa;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.domain.car.Component;
import org.example.domain.car.ComponentId;
import org.example.domain.car.Model;
import org.example.domain.common.exception.EntityNotFoundException;
import org.example.domain.ports.ComponentRepository;
import org.example.domain.ports.ModelRepository;
import org.example.infrastructure.mapper.ComponentJpaMapper;
import org.example.infrastructure.persistence.entity.ComponentJpaEntity;
import org.example.infrastructure.persistence.repository.ComponentJpaRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Primary
@Repository
public class JpaComponentRepository implements ComponentRepository {

    private final ComponentJpaRepository jpaRepository;
    private final ComponentJpaMapper mapper;
    private final ModelRepository modelRepository;

    public JpaComponentRepository(ComponentJpaRepository jpaRepository,
                                  ComponentJpaMapper mapper,
                                  @Lazy ModelRepository modelRepository) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
        this.modelRepository = modelRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Component> findAll() {
        return jpaRepository.findAll().stream()
                .map(entity -> mapper.toDomain(entity, modelRepository))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Component findById(ComponentId componentId) {
        Optional<ComponentJpaEntity> entity = jpaRepository.findById(componentId.value());
        return entity.map(e -> mapper.toDomain(e, modelRepository)).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Component findByName(String name) {
        Optional<ComponentJpaEntity> entity = jpaRepository.findByName(name);
        return entity.map(e -> mapper.toDomain(e, modelRepository)).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Component> findCompatible(Model model) {
        return jpaRepository.findAll().stream()
                .filter(e -> e.getCompatibleModelIds() != null && e.getCompatibleModelIds().contains(model.id().value()))
                .map(e -> mapper.toDomain(e, modelRepository))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Component save(Component component) {
        ComponentJpaEntity entity = mapper.toJpa(component);
        ComponentJpaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved, modelRepository);
    }

    @Override
    @Transactional
    public Component update(Component component) {
        if (!jpaRepository.existsById(component.id().value())) {
            throw new EntityNotFoundException("Component with id " + component.id().value() + " not found");
        }
        ComponentJpaEntity entity = mapper.toJpa(component);
        ComponentJpaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved, modelRepository);
    }

    @Override
    @Transactional
    public void delete(ComponentId componentId) {
        if (!jpaRepository.existsById(componentId.value())) {
            throw new EntityNotFoundException("Component with id " + componentId.value() + " not found");
        }
        jpaRepository.deleteById(componentId.value());
    }

    @Override
    @Transactional
    public void addCompatibility(ComponentId componentId, Model model) {
        ComponentJpaEntity entity = jpaRepository.findById(componentId.value())
                .orElseThrow(() -> new EntityNotFoundException("Component not found"));
        Set<UUID> compatibleModels = new HashSet<>(entity.getCompatibleModelIds());
        compatibleModels.add(model.id().value());
        entity.setCompatibleModelIds(compatibleModels);
        jpaRepository.save(entity);
    }

    @Override
    @Transactional
    public void removeCompatibility(ComponentId componentId, Model model) {
        ComponentJpaEntity entity = jpaRepository.findById(componentId.value())
                .orElseThrow(() -> new EntityNotFoundException("Component not found"));
        Set<UUID> compatibleModels = new HashSet<>(entity.getCompatibleModelIds());
        if (!compatibleModels.remove(model.id().value())) {
            throw new EntityNotFoundException("Compatibility not found");
        }
        entity.setCompatibleModelIds(compatibleModels);
        jpaRepository.save(entity);
    }
}