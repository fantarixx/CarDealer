package org.example.repository;

import org.example.PostgresIT;
import org.example.domain.car.ComponentType;
import org.example.infrastructure.persistence.repository.ComponentJpaRepository;
import org.example.infrastructure.persistence.entity.ComponentJpaEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class ComponentJpaRepositoryTest extends PostgresIT {

    @Autowired
    private ComponentJpaRepository repository;

    @Test
    void shouldSaveAndFindComponent() {
        ComponentJpaEntity component = new ComponentJpaEntity();
        component.setType(ComponentType.WHEELS);
        component.setName("Test Wheel");
        component.setPrice(10000L);
        component.setCreatedAt(Instant.now());
        component.setUpdatedAt(Instant.now());

        ComponentJpaEntity saved = repository.save(component);
        assertThat(saved.getId()).isNotNull();

        ComponentJpaEntity found = repository.findById(saved.getId()).orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Test Wheel");
    }
}