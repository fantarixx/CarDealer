package org.example;

import org.example.domain.user.RoleType;
import org.example.domain.user.User;
import org.example.domain.user.UserId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class UserTest {

    @Test
    void shouldCreateUserWithValidData() {
        UserId id = new UserId();
        User user = new User(id, "john", RoleType.CLIENT);
        assertThat(user.getId()).isEqualTo(id);
        assertThat(user.getUserName()).isEqualTo("john");
        assertThat(user.getRole()).isEqualTo(RoleType.CLIENT);
    }

    @Test
    void shouldThrowWhenUserNameIsNull() {
        assertThatThrownBy(() -> new User(new UserId(), null, RoleType.CLIENT))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("User name cannot be null.");
    }

    @Test
    void clientPermissions() {
        User client = new User(new UserId(), "client", RoleType.CLIENT);
        assertThat(client.canViewCars()).isTrue();
        assertThat(client.canCreateOrder()).isTrue();
        assertThat(client.canManageTestOrders()).isFalse();
        assertThat(client.canManageStock()).isFalse();
        assertThat(client.canManageEntities()).isFalse();
    }

    @Test
    void salonManagerPermissions() {
        User manager = new User(new UserId(), "manager", RoleType.SALON_MANAGER);
        assertThat(manager.canViewCars()).isTrue();
        assertThat(manager.canCreateOrder()).isFalse();
        assertThat(manager.canManageTestOrders()).isTrue();
        assertThat(manager.canManageStock()).isFalse();
        assertThat(manager.canManageEntities()).isFalse();
    }

    @Test
    void warehouseAdminPermissions() {
        User admin = new User(new UserId(), "warehouse", RoleType.WAREHOUSE_ADMIN);
        assertThat(admin.canViewCars()).isTrue();
        assertThat(admin.canCreateOrder()).isFalse();
        assertThat(admin.canManageTestOrders()).isFalse();
        assertThat(admin.canManageStock()).isTrue();
        assertThat(admin.canManageEntities()).isFalse();
    }

    @Test
    void systemAdminPermissions() {
        User sysAdmin = new User(new UserId(), "sysadmin", RoleType.SYSTEM_ADMIN);
        assertThat(sysAdmin.canViewCars()).isTrue();
        assertThat(sysAdmin.canCreateOrder()).isTrue();
        assertThat(sysAdmin.canManageTestOrders()).isTrue();
        assertThat(sysAdmin.canManageStock()).isTrue();
        assertThat(sysAdmin.canManageEntities()).isTrue();
    }
}
