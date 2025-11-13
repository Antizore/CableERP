package com.example.CableERP.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class CustomerOrderItem {

    protected CustomerOrderItem(){}

    public CustomerOrderItem(Long id, String order, String product){
        this.id = id;
        this.order = order;
        this.product = product;
    }



    @Id
    private Long id;
    private String order;
    private String product;
    private double qty;

}
