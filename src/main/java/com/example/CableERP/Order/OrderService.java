package com.example.CableERP.Order;


import com.example.CableERP.Customer.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.sql.Timestamp;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository){
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
    }

    public List<Order> returnAllOrders(){
        return orderRepository.findAll();
    }

    public Order returnOrderById(Long id){
        return orderRepository.findById(id).orElseThrow();
    }

    public Order saveOrderToDB(CreateOrderDTO customerOrderDTO){
        Calendar rightNow = Calendar.getInstance();

        Order order = new Order(
                customerRepository.findById(customerOrderDTO.customerId()).orElseThrow(),
                customerOrderDTO.orderNumber(),
                OrderStatus.NEW,
                new Timestamp(rightNow.getTimeInMillis()),
                new Timestamp(rightNow.getTimeInMillis())
        );

        return orderRepository.saveAndFlush(order);
    }

}
