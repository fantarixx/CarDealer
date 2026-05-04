package org.example.repository.memory;

import org.example.domain.common.exception.EntityNotFoundException;
import org.example.domain.user.RoleType;
import org.example.domain.user.User;
import org.example.domain.user.UserId;
import org.example.infrastructure.repository.memory.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {

    private UserRepository repository;

    @BeforeEach
    void setUp() throws Exception {
        repository = new UserRepository();
        Field field = UserRepository.class.getDeclaredField("users");
        field.setAccessible(true);
        Map<?, ?> map = (Map<?, ?>) field.get(null);
        map.clear();
    }

    private User createUser(String uuidStr, String name, RoleType role) {
        UserId userId = new UserId(UUID.fromString(uuidStr));
        return new User(userId, name, role);
    }

    @Test
    void testSaveAndFindById() {
        User user = createUser("00000000-0000-0000-0000-000000000001", "John", RoleType.CLIENT);
        repository.save(user);

        UserId id = new UserId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        User found = repository.findById(id);
        assertNotNull(found);
        assertEquals("John", found.getUserName());
        assertEquals(RoleType.CLIENT, found.getRole());
    }

    @Test
    void testFindAll() {
        User user1 = createUser("00000000-0000-0000-0000-000000000001", "John", RoleType.CLIENT);
        User user2 = createUser("00000000-0000-0000-0000-000000000002", "Jane", RoleType.SALON_MANAGER);
        repository.save(user1);
        repository.save(user2);

        List<User> all = repository.findAll();
        assertEquals(2, all.size());
        assertTrue(all.contains(user1));
        assertTrue(all.contains(user2));
    }

    @Test
    void testFindByName() {
        User user = createUser("00000000-0000-0000-0000-000000000001", "John", RoleType.CLIENT);
        repository.save(user);

        User found = repository.findByName("John");
        assertNotNull(found);
        assertEquals("John", found.getUserName());
        assertEquals(RoleType.CLIENT, found.getRole());

        assertNull(repository.findByName("Unknown"));
    }

    @Test
    void testUpdateExisting() {
        User user = createUser("00000000-0000-0000-0000-000000000001", "John", RoleType.CLIENT);
        repository.save(user);

        User updated = createUser("00000000-0000-0000-0000-000000000001", "Johnny", RoleType.SYSTEM_ADMIN);
        repository.update(updated);

        UserId id = new UserId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        User found = repository.findById(id);
        assertEquals("Johnny", found.getUserName());
        assertEquals(RoleType.SYSTEM_ADMIN, found.getRole());
    }

    @Test
    void testUpdateNonExistingThrowsException() {
        User user = createUser("00000000-0000-0000-0000-000000000001", "John", RoleType.CLIENT);
        assertThrows(EntityNotFoundException.class, () -> repository.update(user));
    }

    @Test
    void testDeleteExisting() {
        User user = createUser("00000000-0000-0000-0000-000000000001", "John", RoleType.CLIENT);
        repository.save(user);
        UserId id = new UserId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        repository.delete(id);

        assertNull(repository.findById(id));
        assertEquals(0, repository.findAll().size());
    }

    @Test
    void testDeleteNonExistingThrowsException() {
        UserId id = new UserId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        assertThrows(EntityNotFoundException.class, () -> repository.delete(id));
    }
}