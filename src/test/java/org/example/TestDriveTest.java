package org.example;

import org.example.domain.car.CarId;
import org.example.domain.common.exception.DomainValidationException;
import org.example.domain.order.TestDrive;
import org.example.domain.order.TestDriveId;
import org.example.domain.order.TestDriveState;
import org.example.domain.user.UserId;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class TestDriveTest {

    private final TestDriveId id = new TestDriveId();
    private final UserId clientId = new UserId();
    private final CarId carId = new CarId();
    private final LocalDateTime futureTime = LocalDateTime.now().plusDays(1);

    @Test
    void shouldCreateValidTestDrive() {
        TestDrive testDrive = new TestDrive(id, clientId, carId, futureTime);
        assertThat(testDrive.getId()).isEqualTo(id);
        assertThat(testDrive.getCurrentState()).isEqualTo(TestDriveState.FORMED);
    }

    @Test
    void shouldThrowWhenDateInPast() {
        LocalDateTime pastTime = LocalDateTime.now().minusHours(1);
        assertThatThrownBy(() -> new TestDrive(id, clientId, carId, pastTime))
                .isInstanceOf(DomainValidationException.class).hasMessage("Incorrect date.");
    }

    @Test
    void shouldTransitionToScheduled() {
        TestDrive testDrive = new TestDrive(id, clientId, carId, futureTime);
        testDrive.schedule();
        assertThat(testDrive.getCurrentState()).isEqualTo(TestDriveState.SCHEDULED);
    }

    @Test
    void shouldComplete() {
        TestDrive testDrive = new TestDrive(id, clientId, carId, futureTime);
        testDrive.schedule();
        testDrive.complete();
        assertThat(testDrive.getCurrentState()).isEqualTo(TestDriveState.COMPLETED);
    }

    @Test
    void shouldCancelWhenFormed() {
        TestDrive testDrive = new TestDrive(id, clientId, carId, futureTime);
        testDrive.cancel();
        assertThat(testDrive.getCurrentState()).isEqualTo(TestDriveState.CANCELLED);
    }

    @Test
    void shouldThrowWhenSchedulingAlreadyScheduled() {
        TestDrive testDrive = new TestDrive(id, clientId, carId, futureTime);
        testDrive.schedule();
        assertThatThrownBy(testDrive::schedule).isInstanceOf(DomainValidationException.class);
    }
}
