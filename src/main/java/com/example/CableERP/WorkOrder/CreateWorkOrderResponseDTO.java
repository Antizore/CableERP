package com.example.CableERP.WorkOrder;

public record CreateWorkOrderResponseDTO(Long id, Long productId, Double qty, WorkOrderStatus status) { }
