package org.example.domain.order;

public enum OrderState {
    FORMED,
    CONFIRMED,
    PENDING_PAYMENT,
    PAID,
    PENDING_DELIVERY,
    READY,
    COMPLETED,
    CANCELLED
}
