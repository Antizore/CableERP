package com.example.CableERP.service;


import com.example.CableERP.DTOs.ReservingComponentDTO;
import com.example.CableERP.entity.Component;
import com.example.CableERP.entity.Reservation;
import com.example.CableERP.enums.Status;
import com.example.CableERP.repository.ComponentRepository;
import com.example.CableERP.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

@Service
public class ReservationService {

    final ReservationRepository reservationRepository;
    final ComponentRepository componentRepository;

    public ReservationService(ReservationRepository reservationRepository, ComponentRepository componentRepository){
        this.reservationRepository = reservationRepository;
        this.componentRepository = componentRepository;
    }

    public List<Reservation> getAllReservations (){
        return reservationRepository.findAll();
    }


    public void froze(ReservingComponentDTO reservingComponentDTO){

        Component component = componentRepository.findById(reservingComponentDTO.componentId()).get();
        Calendar rightNow = Calendar.getInstance();
        Reservation reservation = new Reservation(null,component, reservingComponentDTO.qty(), Status.FROZEN,new Timestamp(rightNow.getTimeInMillis()));
        reservationRepository.saveAndFlush(reservation);

    }


    public void release(){

    }


}
