package org.example.domain.user;

public interface Role {
    boolean canViewCars();
    boolean canCreateOrder();
    boolean canManageTestOrders();
    boolean canManageStock();
    boolean canManageEntities();
}
