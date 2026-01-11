package com.example.CableERP.Customer.CustomerOrder;


import com.example.CableERP.Product.Product;
import com.example.CableERP.Product.ProductCreateDTO;
import jakarta.persistence.*;

import java.util.function.DoubleBinaryOperator;

@Entity
@Table(name = "customer_order_item")
public class OrderItem {

    protected OrderItem(){}

    public OrderItem(Order order, Product product, Double qty){
        this.order = order;
        this.qty = qty;
        this.product = product;
    }



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    private double qty;


    public Long getId() {
        return id;
    }


    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }
}
