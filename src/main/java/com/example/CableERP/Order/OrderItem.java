package com.example.CableERP.Order;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class OrderItem {

    protected OrderItem(){}

    public OrderItem(String order, String product){
        this.order = order;
        this.product = product;
    }



    @Id
    private Long id;
    private String order;
    private String product;
    private double qty;

}
