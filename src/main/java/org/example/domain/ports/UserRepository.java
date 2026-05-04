package org.example.domain.ports;

import org.example.domain.user.User;
import org.example.domain.user.UserId;

import java.util.List;

public interface UserRepository {
    List<User> findAll();
    User findById(UserId userId);
    User findByName(String userName);
    User save(User user);
    User update(User user);
    void delete(UserId userId);
}

