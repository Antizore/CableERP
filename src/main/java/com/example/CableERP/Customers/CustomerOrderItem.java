package com.example.CableERP.Customers;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class CustomerOrderItem {

    protected CustomerOrderItem(){}

    public CustomerOrderItem(String order, String product){
        this.order = order;
        this.product = product;
    }



    @Id
    private Long id;
    private String order;
    private String product;
    private double qty;

}
