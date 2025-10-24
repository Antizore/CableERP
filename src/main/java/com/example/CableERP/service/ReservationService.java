package com.example.CableERP.service;


import com.example.CableERP.repository.ReservationRepository;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {

    final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository){
        this.reservationRepository = reservationRepository;
    }



    public void froze(){

    }


    public void release(){

    }


}
