package com.example.CableERP.service;


import com.example.CableERP.DTOs.CreateOrderDTO;
import com.example.CableERP.entity.CustomerOrder;
import com.example.CableERP.enums.Status;
import com.example.CableERP.repository.CustomerOrderRepository;
import com.example.CableERP.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.List;
import java.sql.Timestamp;

@Service
public class CustomerOrderService {

    private final CustomerOrderRepository customerOrderRepository;
    private final CustomerRepository customerRepository;

    public CustomerOrderService(CustomerOrderRepository customerOrderRepository,CustomerRepository customerRepository){
        this.customerOrderRepository = customerOrderRepository;
        this.customerRepository = customerRepository;
    }

    public List<CustomerOrder> returnAllOrders(){
        return customerOrderRepository.findAll();
    }

    public CustomerOrder returnOrderById(Long id){
        return customerOrderRepository.findById(id).orElse(null);
    }

    public void saveOrderToDB(CreateOrderDTO customerOrderDTO){
        Calendar rightNow = Calendar.getInstance();


        CustomerOrder customerOrder = new CustomerOrder(
                customerRepository.findById(customerOrderDTO.customerId()).orElse(null),
                customerOrderDTO.orderNumber(),
                Status.NEW,
                new Timestamp(rightNow.getTimeInMillis()),
                new Timestamp(rightNow.getTimeInMillis())
        );


        customerOrderRepository.saveAndFlush(customerOrder);
    }

}
