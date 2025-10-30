package com.example.CableERP.entity;


import jakarta.persistence.Entity;

@Entity
public class CustomerOrderItem {


    private Long id;
    private CustomerOrder order;
    private Product product;
    private double qty;

}
