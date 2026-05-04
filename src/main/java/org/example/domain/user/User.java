package org.example.domain.user;

import lombok.Getter;

import java.util.Objects;

@Getter
public class User {
    private final UserId id;

    private final String userName;
    private final RoleType role;

    public User(UserId id, String userName, RoleType role) {
        this.id = Objects.requireNonNull(id, "User id cannot be null.");
        this.userName = Objects.requireNonNull(userName, "User name cannot be null.");
        this.role = Objects.requireNonNull(role, "Role cannot be null.");
    }

    public boolean canViewCars() {
        return role.canViewCars(); }
    public boolean canCreateOrder() {
        return role.canCreateOrder(); }
    public boolean canManageTestOrders() {
        return role.canManageTestOrders  (); }
    public boolean canManageStock() {
        return role.canManageStock(); }
    public boolean canManageEntities() {
        return role.canManageEntities(); }
}
