package com.example.CableERP.Reservation;

public record ReservationRequestDTO(
        Long orderId,
        Long componentId,
        double qty
) {}