package org.example.infrastructure.repository.jpa;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.domain.car.Car;
import org.example.domain.car.CarId;
import org.example.domain.common.exception.EntityNotFoundException;
import org.example.domain.ports.CarRepository;
import org.example.infrastructure.mapper.CarJpaMapper;
import org.example.infrastructure.persistence.entity.CarJpaEntity;
import org.example.infrastructure.persistence.repository.CarJpaRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Primary
@Repository
public class JpaCarRepository implements CarRepository {
    private final CarJpaRepository jpaRepository;
    private final CarJpaMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<Car> findAll() {
        return jpaRepository.findAll().stream()
                .map(entity -> mapper.toDomain(entity, null))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Car findById(CarId carId) {
        Optional<CarJpaEntity> entity = jpaRepository.findById(carId.value());
        return entity.map(e -> mapper.toDomain(e, null)).orElse(null);
    }

    @Override
    @Transactional
    public Car save(Car car) {
        CarJpaEntity entity = mapper.toJpa(car);
        CarJpaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved, null);
    }

    @Override
    @Transactional
    public Car update(Car car) {
        if (!jpaRepository.existsById(car.getId().value())) {
            throw new EntityNotFoundException("Car with id " + car.getId().value() + " not found");
        }
        CarJpaEntity entity = mapper.toJpa(car);
        CarJpaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved, null);
    }

    @Override
    @Transactional
    public void delete(CarId carId) {
        if (!jpaRepository.existsById(carId.value())) {
            throw new EntityNotFoundException("Car with id " + carId.value() + " not found");
        }
        jpaRepository.deleteById(carId.value());
    }
}