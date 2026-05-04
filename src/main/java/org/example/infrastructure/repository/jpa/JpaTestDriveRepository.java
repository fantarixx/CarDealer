package org.example.infrastructure.repository.jpa;

import lombok.AllArgsConstructor;
import org.example.domain.order.TestDrive;
import org.example.domain.order.TestDriveId;
import org.example.domain.common.exception.EntityNotFoundException;
import org.example.domain.ports.TestDriveRepository;
import org.example.infrastructure.mapper.TestDriveJpaMapper;
import org.example.infrastructure.persistence.entity.TestDriveJpaEntity;
import org.example.infrastructure.persistence.repository.TestDriveJpaRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Primary
@Repository
public class JpaTestDriveRepository implements TestDriveRepository {
    private final TestDriveJpaRepository jpaRepository;
    private final TestDriveJpaMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<TestDrive> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TestDrive findById(TestDriveId testDriveId) {
        Optional<TestDriveJpaEntity> entity = jpaRepository.findById(testDriveId.value());
        return entity.map(mapper::toDomain).orElse(null);
    }

    @Override
    @Transactional
    public TestDrive save(TestDrive testDrive) {
        TestDriveJpaEntity entity = mapper.toJpa(testDrive);
        TestDriveJpaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    @Transactional
    public TestDrive update(TestDrive testDrive) {
        if (!jpaRepository.existsById(testDrive.getId().value())) {
            throw new EntityNotFoundException("TestDrive with id " + testDrive.getId().value() + " not found");
        }
        TestDriveJpaEntity entity = mapper.toJpa(testDrive);
        TestDriveJpaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    @Transactional
    public void delete(TestDriveId testDriveId) {
        if (!jpaRepository.existsById(testDriveId.value())) {
            throw new EntityNotFoundException("TestDrive with id " + testDriveId.value() + " not found");
        }
        jpaRepository.deleteById(testDriveId.value());
    }
}