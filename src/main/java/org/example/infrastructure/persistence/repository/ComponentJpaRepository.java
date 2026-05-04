package org.example.infrastructure.persistence.repository;

import org.example.infrastructure.persistence.entity.ComponentJpaEntity;
import org.example.domain.car.ComponentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ComponentJpaRepository extends JpaRepository<ComponentJpaEntity, UUID> {
    Optional<ComponentJpaEntity> findByName(String name);
    List<ComponentJpaEntity> findByType(ComponentType type);
}