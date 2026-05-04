package org.example.infrastructure.repository.memory;

import org.example.domain.common.exception.EntityNotFoundException;
import org.example.domain.order.TestDrive;
import org.example.domain.order.TestDriveId;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class TestDriveRepository implements org.example.domain.ports.TestDriveRepository {
    private static final Map<TestDriveId, TestDrive> testDrives = new ConcurrentHashMap<>();

    @Override
    public List<TestDrive> findAll() {
        return new ArrayList<>(testDrives.values());
    }

    @Override
    public TestDrive findById(TestDriveId testDriveId) {
        return testDrives.get(testDriveId);
    }

    @Override
    public TestDrive save(TestDrive testDrive) {
        testDrives.put(testDrive.getId(), testDrive);
        return testDrive;
    }

    @Override
    public TestDrive update(TestDrive testDrive) {
        TestDriveId newId = testDrive.getId();

        if (!testDrives.containsKey(newId)) {
            throw new EntityNotFoundException("Test drive with id: " + testDrive.getId().value() + " not found.");
        }

        testDrives.put(newId, testDrive);
        return testDrive;
    }

    @Override
    public void delete(TestDriveId testDriveId) {
        if (!testDrives.containsKey(testDriveId)) {
            throw new EntityNotFoundException("Test drive with id: " + testDriveId.value() + " not found.");
        }

        testDrives.remove(testDriveId);
    }
}
