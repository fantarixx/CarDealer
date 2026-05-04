package org.example;

import org.example.domain.car.CarId;
import org.example.domain.order.OrderId;
import org.example.domain.order.TestDriveId;
import org.example.domain.user.UserId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class IdTest {

    @Test
    void carIdShouldBeCreatedWithGivenValue() {
        UUID uuid = UUID.randomUUID();
        CarId carId = new CarId(uuid);
        assertThat(carId.value()).isEqualTo(uuid);
    }

    @Test
    void carIdShouldThrowWhenNull() {
        assertThatThrownBy(() -> new CarId(null)).isInstanceOf(IllegalArgumentException.class).hasMessage("CarId can't be null");
    }

    @Test
    void carIdShouldGenerateRandomWhenNoArg() {
        CarId carId = new CarId();
        assertThat(carId.value()).isNotNull();
    }

    @Test
    void orderIdShouldWork() {
        UUID uuid = UUID.randomUUID();
        OrderId orderId = new OrderId(uuid);
        assertThat(orderId.value()).isEqualTo(uuid);
        assertThatThrownBy(() -> new OrderId(null)).isInstanceOf(IllegalArgumentException.class).hasMessage("OrderId can't be null");
    }

    @Test
    void testDriveIdShouldWork() {
        UUID uuid = UUID.randomUUID();
        TestDriveId id = new TestDriveId(uuid);
        assertThat(id.value()).isEqualTo(uuid);
        assertThatThrownBy(() -> new TestDriveId(null))
                .isInstanceOf(IllegalArgumentException.class).hasMessage("TestDriveId can't be null");
    }

    @Test
    void userIdShouldWork() {
        UUID uuid = UUID.randomUUID();
        UserId userId = new UserId(uuid);
        assertThat(userId.value()).isEqualTo(uuid);
        assertThatThrownBy(() -> new UserId(null)).isInstanceOf(IllegalArgumentException.class).hasMessage("UserId can't be null");
    }
}
