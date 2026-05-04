package org.example.infrastructure.mapper;

import org.example.domain.user.User;
import org.example.domain.user.UserId;
import org.example.infrastructure.persistence.entity.UserJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class UserJpaMapper {

    public User toDomain(UserJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        return new User(new UserId(entity.getId()), entity.getUserName(), entity.getRole());
    }

    public UserJpaEntity toJpa(User user) {
        if (user == null) {
            return null;
        }
        UserJpaEntity entity = new UserJpaEntity();
        entity.setId(user.getId().value());
        entity.setUserName(user.getUserName());
        entity.setRole(user.getRole());
        return entity;
    }
}