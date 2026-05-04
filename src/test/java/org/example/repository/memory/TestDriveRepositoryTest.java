package org.example.repository.memory;

import org.example.domain.car.CarId;
import org.example.domain.common.exception.EntityNotFoundException;
import org.example.domain.order.TestDrive;
import org.example.domain.order.TestDriveId;
import org.example.domain.order.TestDriveState;
import org.example.domain.user.UserId;
import org.example.infrastructure.repository.memory.TestDriveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TestDriveRepositoryTest {

    private TestDriveRepository repository;

    @BeforeEach
    void setUp() throws Exception {
        repository = new TestDriveRepository();
        Field field = TestDriveRepository.class.getDeclaredField("testDrives");
        field.setAccessible(true);
        Map<?, ?> map = (Map<?, ?>) field.get(null);
        map.clear();
    }

    private TestDrive createTestDrive(String testDriveUuid, String clientUuid, String carUuid, LocalDateTime scheduledAt) {
        TestDriveId testDriveId = new TestDriveId(UUID.fromString(testDriveUuid));
        UserId clientId = new UserId(UUID.fromString(clientUuid));
        CarId carId = new CarId(UUID.fromString(carUuid));
        return new TestDrive(testDriveId, clientId, carId, scheduledAt);
    }

    @Test
    void testSaveAndFindById() {
        LocalDateTime scheduledAt = LocalDateTime.now().plusDays(1);
        TestDrive td = createTestDrive(
                "00000000-0000-0000-0000-000000000001",
                "11111111-1111-1111-1111-111111111111",
                "22222222-2222-2222-2222-222222222222",
                scheduledAt
        );
        repository.save(td);

        TestDriveId id = new TestDriveId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        TestDrive found = repository.findById(id);
        assertNotNull(found);
        assertEquals(id.value(), found.getId().value());
        assertEquals(new UserId(UUID.fromString("11111111-1111-1111-1111-111111111111")), found.getClientId());
        assertEquals(new CarId(UUID.fromString("22222222-2222-2222-2222-222222222222")), found.getCarId());
        assertEquals(scheduledAt, found.getScheduledAt());
        assertEquals(TestDriveState.FORMED, found.getCurrentState());
    }

    @Test
    void testFindAll() {
        LocalDateTime scheduledAt = LocalDateTime.now().plusDays(1);
        TestDrive td1 = createTestDrive(
                "00000000-0000-0000-0000-000000000001",
                "11111111-1111-1111-1111-111111111111",
                "22222222-2222-2222-2222-222222222222",
                scheduledAt
        );
        TestDrive td2 = createTestDrive(
                "00000000-0000-0000-0000-000000000002",
                "33333333-3333-3333-3333-333333333333",
                "44444444-4444-4444-4444-444444444444",
                scheduledAt.plusDays(2)
        );
        repository.save(td1);
        repository.save(td2);

        List<TestDrive> all = repository.findAll();
        assertEquals(2, all.size());
        assertTrue(all.contains(td1));
        assertTrue(all.contains(td2));
    }

    @Test
    void testUpdateExisting() {
        LocalDateTime scheduledAt = LocalDateTime.now().plusDays(1);
        TestDrive td = createTestDrive(
                "00000000-0000-0000-0000-000000000001",
                "11111111-1111-1111-1111-111111111111",
                "22222222-2222-2222-2222-222222222222",
                scheduledAt
        );
        repository.save(td);

        // Создаём обновлённую версию с новыми данными
        LocalDateTime newScheduledAt = LocalDateTime.now().plusDays(3);
        TestDrive updated = createTestDrive(
                "00000000-0000-0000-0000-000000000001",
                "55555555-5555-5555-5555-555555555555", // новый clientId
                "66666666-6666-6666-6666-666666666666", // новый carId
                newScheduledAt
        );
        repository.update(updated);

        TestDriveId id = new TestDriveId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        TestDrive found = repository.findById(id);
        assertNotNull(found);
        assertEquals(new UserId(UUID.fromString("55555555-5555-5555-5555-555555555555")), found.getClientId());
        assertEquals(new CarId(UUID.fromString("66666666-6666-6666-6666-666666666666")), found.getCarId());
        assertEquals(newScheduledAt, found.getScheduledAt());
        assertEquals(TestDriveState.FORMED, found.getCurrentState());
    }

    @Test
    void testUpdateNonExistingThrowsException() {
        LocalDateTime scheduledAt = LocalDateTime.now().plusDays(1);
        TestDrive td = createTestDrive(
                "00000000-0000-0000-0000-000000000001",
                "11111111-1111-1111-1111-111111111111",
                "22222222-2222-2222-2222-222222222222",
                scheduledAt
        );
        assertThrows(EntityNotFoundException.class, () -> repository.update(td));
    }

    @Test
    void testDeleteExisting() {
        LocalDateTime scheduledAt = LocalDateTime.now().plusDays(1);
        TestDrive td = createTestDrive(
                "00000000-0000-0000-0000-000000000001",
                "11111111-1111-1111-1111-111111111111",
                "22222222-2222-2222-2222-222222222222",
                scheduledAt
        );
        repository.save(td);
        TestDriveId id = new TestDriveId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        repository.delete(id);

        assertNull(repository.findById(id));
        assertEquals(0, repository.findAll().size());
    }

    @Test
    void testDeleteNonExistingThrowsException() {
        TestDriveId id = new TestDriveId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        assertThrows(EntityNotFoundException.class, () -> repository.delete(id));
    }
}