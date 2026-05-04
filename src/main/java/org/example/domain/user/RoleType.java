package org.example.domain.user;

public enum RoleType implements Role {
    CLIENT {
        public boolean canViewCars() { return true; }
        public boolean canCreateOrder() { return true; }
        public boolean canManageTestOrders() { return false; }
        public boolean canManageStock() { return false; }
        public boolean canManageEntities() { return false; }
    },

    SALON_MANAGER {
        public boolean canViewCars() { return true; }
        public boolean canCreateOrder() { return false; }
        public boolean canManageTestOrders() { return true; }
        public boolean canManageStock() { return false; }
        public boolean canManageEntities() { return false; }
    },

    WAREHOUSE_ADMIN {
        public boolean canViewCars() { return true; }
        public boolean canCreateOrder() { return false; }
        public boolean canManageTestOrders() { return false; }
        public boolean canManageStock() { return true; }
        public boolean canManageEntities() { return false; }
    },

    SYSTEM_ADMIN {
        public boolean canViewCars() { return true; }
        public boolean canCreateOrder() { return true; }
        public boolean canManageTestOrders() { return true; }
        public boolean canManageStock() { return true; }
        public boolean canManageEntities() { return true; }
    }
}

