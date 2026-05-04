package org.example.infrastructure.persistence.repository;

import org.example.infrastructure.persistence.entity.ModelJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ModelJpaRepository extends JpaRepository<ModelJpaEntity, UUID>, JpaSpecificationExecutor<ModelJpaEntity> {
    Optional<ModelJpaEntity> findByName(String name);
}