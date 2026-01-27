package com.example.CableERP.Component;


import com.example.CableERP.Common.Unit;
import com.example.CableERP.Procurement.Procurement;
import com.example.CableERP.Reservation.Reservation;

import java.util.List;


public record ComponentResponseDTO(
        Long id,
        String name,
        Unit unit,
        Double costPerUnit
        //Procurement procurement
        //List<Reservation> reservationList

) {
}
