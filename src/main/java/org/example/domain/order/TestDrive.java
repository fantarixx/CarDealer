package org.example.domain.order;

import lombok.Getter;
import lombok.Setter;
import org.example.domain.car.CarId;
import org.example.domain.common.exception.DomainValidationException;
import org.example.domain.user.UserId;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class TestDrive {
    private final TestDriveId id;
    private final UserId clientId;
    private final CarId carId;
    private final LocalDateTime scheduledAt;
    @Setter
    private TestDriveState currentState;

    public TestDrive(TestDriveId id, UserId clientId, CarId carId, LocalDateTime scheduledAt) {
        this.id = Objects.requireNonNull(id, "Test drive id cannot be null.");
        this.clientId = Objects.requireNonNull(clientId, "Client id in test drive cannot be null.");
        this.carId = Objects.requireNonNull(carId, "Car id in test drive cannot be null.");
        this.scheduledAt = Objects.requireNonNull(scheduledAt, "Test drive date cannot be null.");
        if (scheduledAt.isBefore(LocalDateTime.now())) {
            throw new DomainValidationException("Incorrect date.");
        }
        currentState = TestDriveState.FORMED;
    }

    public void schedule() {
        if (currentState != TestDriveState.FORMED) {
            throw new DomainValidationException("Test drive must be FORMED to make it SCHEDULED.");
        }
        currentState = TestDriveState.SCHEDULED;
    }

    public void complete() {
        if (currentState != TestDriveState.SCHEDULED) {
            throw new DomainValidationException("Test drive must be SCHEDULED to make it COMPLETED.");
        }
        currentState = TestDriveState.COMPLETED;
    }

    public void cancel() {
        if (currentState != TestDriveState.SCHEDULED && currentState != TestDriveState.FORMED) {
            throw new DomainValidationException("Test drive can't already be cancelled.");
        }
        currentState = TestDriveState.CANCELLED;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof TestDrive testDrive && id.equals(testDrive.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
