package com.example.CableERP.WorkOrder;

import java.sql.Timestamp;

public record ReturnAllWorkOrdersDTO (
        Long id,
        String productName,
        Double qty,
        WorkOrderStatus workOrderStatus,
        Timestamp createdAt,
        Timestamp startedAt,
        Timestamp finishedAt) { }
