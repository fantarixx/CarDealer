package org.example.domain.ports;

import org.example.domain.order.TestDrive;
import org.example.domain.order.TestDriveId;

import java.util.List;

public interface TestDriveRepository {
    List<TestDrive> findAll();
    TestDrive findById(TestDriveId testDriveId);
    TestDrive save(TestDrive testDrive);
    TestDrive update(TestDrive testDrive);
    void delete(TestDriveId testDriveId);
}
