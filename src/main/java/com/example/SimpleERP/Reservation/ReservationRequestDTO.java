package com.example.SimpleERP.Reservation;

public record ReservationRequestDTO(
        Long orderId,
        Long componentId,
        double qty
) {}