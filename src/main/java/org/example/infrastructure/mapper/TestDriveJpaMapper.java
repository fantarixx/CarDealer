package org.example.infrastructure.mapper;

import org.example.domain.car.CarId;
import org.example.domain.order.TestDrive;
import org.example.domain.order.TestDriveId;
import org.example.domain.user.UserId;
import org.example.infrastructure.persistence.entity.TestDriveJpaEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class TestDriveJpaMapper {

    public TestDrive toDomain(TestDriveJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        TestDrive testDrive = new TestDrive(
                new TestDriveId(entity.getId()),
                new UserId(entity.getClientId()),
                new CarId(entity.getCarId()),
                LocalDateTime.ofInstant(entity.getScheduledAt(), ZoneId.systemDefault())
        );
        testDrive.setCurrentState(entity.getState());

        return testDrive;
    }

    public TestDriveJpaEntity toJpa(TestDrive testDrive) {
        if (testDrive == null) {
            return null;
        }

        TestDriveJpaEntity entity = new TestDriveJpaEntity();
        entity.setId(testDrive.getId().value());
        entity.setClientId(testDrive.getClientId().value());
        entity.setCarId(testDrive.getCarId().value());
        entity.setScheduledAt(testDrive.getScheduledAt().atZone(ZoneId.systemDefault()).toInstant());
        entity.setState(testDrive.getCurrentState());
        return entity;
    }
}