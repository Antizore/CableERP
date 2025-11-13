package com.example.CableERP.service;


import com.example.CableERP.entity.CustomerOrder;
import com.example.CableERP.repository.CustomerOrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerOrderService {

    private final CustomerOrderRepository customerOrderRepository;

    public CustomerOrderService(CustomerOrderRepository customerOrderRepository){
        this.customerOrderRepository = customerOrderRepository;
    }

    public List<CustomerOrder> returnAllOrders(){
        return customerOrderRepository.findAll();
    }

    public CustomerOrder returnOrderById(Long id){
        return customerOrderRepository.findById(id).orElse(null);
    }


}
