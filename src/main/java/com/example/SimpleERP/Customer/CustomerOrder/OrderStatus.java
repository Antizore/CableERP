package com.example.SimpleERP.Customer.CustomerOrder;

public enum OrderStatus {
    NEW,
    VALIDATING,
    WAITING_FOR_COMPONENTS,
    READY_FOR_PRODUCTION,
    IN_PRODUCTION,
    COMPLETED,
    CANCELLED
}
