package com.example.CableERP.Order;


import com.example.CableERP.Customer.Customer;
import jakarta.persistence.*;


import java.sql.Timestamp;

@Entity
@Table(name = "customer_order")
public class Order {

    protected Order(){}

    public Order(Customer customer, String orderNumber, OrderStatus status, Timestamp createdAt, Timestamp updatedAt) {
        this.customer = customer;
        this.orderNumber = orderNumber;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;
    private String orderNumber;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Long getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public OrderStatus getOrderStatus() {
        return status;
    }

    public void setOrderStatus(OrderStatus status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
