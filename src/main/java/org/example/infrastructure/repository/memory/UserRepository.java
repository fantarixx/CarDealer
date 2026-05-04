package org.example.infrastructure.repository.memory;

import org.example.domain.common.exception.EntityNotFoundException;
import org.example.domain.user.User;
import org.example.domain.user.UserId;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UserRepository implements org.example.domain.ports.UserRepository {
    private static final Map<UserId, User> users = new ConcurrentHashMap<>();

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User findById(UserId userId) {
        return users.get(userId);
    }

    @Override
    public User findByName(String userName) {
        return users.values().stream().filter(c -> c.getUserName().equals(userName)).findFirst().orElse(null);
    }

    @Override
    public User save(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        UserId newId = user.getId();

        if (!users.containsKey(newId)) {
            throw new EntityNotFoundException("User " + user.getUserName() + " not found.");
        }

        users.put(newId, user);
        return user;
    }

    @Override
    public void delete(UserId userId) {
        if (!users.containsKey(userId)) {
            throw new EntityNotFoundException("User with id: " + userId.value() + " not found.");
        }

        users.remove(userId);
    }
}

