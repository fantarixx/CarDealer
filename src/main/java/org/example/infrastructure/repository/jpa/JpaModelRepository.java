package org.example.infrastructure.repository.jpa;

import org.example.domain.car.Model;
import org.example.domain.car.ModelId;
import org.example.domain.common.exception.EntityNotFoundException;
import org.example.domain.ports.ComponentRepository;
import org.example.domain.ports.ModelRepository;
import org.example.infrastructure.mapper.ModelJpaMapper;
import org.example.infrastructure.persistence.entity.ModelJpaEntity;
import org.example.infrastructure.persistence.repository.ModelJpaRepository;
import org.example.infrastructure.persistence.specification.ModelSpecification;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Primary
@Repository
public class JpaModelRepository implements ModelRepository {

    private final ModelJpaRepository jpaRepository;
    private final ModelJpaMapper mapper;
    private final ComponentRepository componentRepository;

    public JpaModelRepository(ModelJpaRepository jpaRepository,
                             ModelJpaMapper mapper,
                             @Lazy ComponentRepository componentRepository) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
        this.componentRepository = componentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Model> findAll() {
        return jpaRepository.findAll().stream()
                .map(entity -> mapper.toDomain(entity, componentRepository))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Model findById(ModelId modelId) {
        Optional<ModelJpaEntity> entity = jpaRepository.findById(modelId.value());
        return entity.map(e -> mapper.toDomain(e, componentRepository)).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Model findByName(String modelName) {
        Optional<ModelJpaEntity> entity = jpaRepository.findByName(modelName);
        return entity.map(e -> mapper.toDomain(e, componentRepository)).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Model> findWithFilters(String brand, Set<UUID> componentIds) {
        Specification<ModelJpaEntity> spec = Specification.where(ModelSpecification.byBrand(brand))
                .and(ModelSpecification.hasAnyComponent(componentIds));
        List<ModelJpaEntity> entities = jpaRepository.findAll(spec);
        return entities.stream()
                .map(entity -> mapper.toDomain(entity, componentRepository))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Model save(Model model) {
        ModelJpaEntity entity = mapper.toJpa(model);
        ModelJpaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved, componentRepository);
    }

    @Override
    @Transactional
    public Model update(Model model) {
        if (!jpaRepository.existsById(model.id().value())) {
            throw new EntityNotFoundException("Model with id " + model.id().value() + " not found");
        }
        ModelJpaEntity entity = mapper.toJpa(model);
        ModelJpaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved, componentRepository);
    }

    @Override
    @Transactional
    public void delete(ModelId modelId) {
        if (!jpaRepository.existsById(modelId.value())) {
            throw new EntityNotFoundException("Model with id " + modelId.value() + " not found");
        }
        jpaRepository.deleteById(modelId.value());
    }
}