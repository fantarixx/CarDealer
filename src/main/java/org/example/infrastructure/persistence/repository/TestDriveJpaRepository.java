package org.example.infrastructure.persistence.repository;

import org.example.infrastructure.persistence.entity.TestDriveJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TestDriveJpaRepository extends JpaRepository<TestDriveJpaEntity, UUID> {
    List<TestDriveJpaEntity> findByClientId(UUID clientId);

    List<TestDriveJpaEntity> findByCarId(UUID carId);
}