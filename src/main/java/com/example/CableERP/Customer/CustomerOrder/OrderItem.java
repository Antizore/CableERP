package com.example.CableERP.Customer.CustomerOrder;


import com.example.CableERP.Product.Product;
import jakarta.persistence.*;

@Entity
@Table(name = "customer_order_item")
public class OrderItem {

    protected OrderItem(){}

    public OrderItem(Order order, Product product, double qty){
        this.order = order;
        this.product = product;
        this.qty = qty;
    }



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private double qty;

}
