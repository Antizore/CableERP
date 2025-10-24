package com.example.CableERP.service;


import com.example.CableERP.repository.OrdersRepository;
import org.springframework.stereotype.Service;

@Service
public class OrdersService {

    final OrdersRepository ordersRepository;

    public OrdersService(OrdersRepository ordersRepository){
        this.ordersRepository = ordersRepository;
    }



    public void froze(){

    }


    public void release(){

    }


}
