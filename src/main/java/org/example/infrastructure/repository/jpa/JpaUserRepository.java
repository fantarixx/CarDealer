package org.example.infrastructure.repository.jpa;

import lombok.AllArgsConstructor;
import org.example.domain.user.User;
import org.example.domain.user.UserId;
import org.example.domain.common.exception.EntityNotFoundException;
import org.example.domain.ports.UserRepository;
import org.example.infrastructure.mapper.UserJpaMapper;
import org.example.infrastructure.persistence.entity.UserJpaEntity;
import org.example.infrastructure.persistence.repository.UserJpaRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Primary
@Repository
public class JpaUserRepository implements UserRepository {
    private final UserJpaRepository jpaRepository;
    private final UserJpaMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(UserId userId) {
        Optional<UserJpaEntity> entity = jpaRepository.findById(userId.value());
        return entity.map(mapper::toDomain).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public User findByName(String userName) {
        Optional<UserJpaEntity> entity = jpaRepository.findByUserName(userName);
        return entity.map(mapper::toDomain).orElse(null);
    }

    @Override
    @Transactional
    public User save(User user) {
        UserJpaEntity entity = mapper.toJpa(user);
        UserJpaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    @Transactional
    public User update(User user) {
        if (!jpaRepository.existsById(user.getId().value())) {
            throw new EntityNotFoundException("User with id " + user.getId().value() + " not found");
        }
        UserJpaEntity entity = mapper.toJpa(user);
        UserJpaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    @Transactional
    public void delete(UserId userId) {
        if (!jpaRepository.existsById(userId.value())) {
            throw new EntityNotFoundException("User with id " + userId.value() + " not found");
        }
        jpaRepository.deleteById(userId.value());
    }
}