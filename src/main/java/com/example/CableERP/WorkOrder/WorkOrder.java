package com.example.CableERP.WorkOrder;

import com.example.CableERP.Product.Product;
import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
public class WorkOrder {

    protected WorkOrder(){}

    public WorkOrder(Product product, Double qty, WorkOrderStatus status, Timestamp createdAt, Timestamp startedAt, Timestamp finishedAt) {
        this.product = product;
        this.qty = qty;
        this.status = status;
        this.createdAt = createdAt;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
    }

    private Long id;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private Double qty;
    @Enumerated(EnumType.STRING)
    private WorkOrderStatus status;
    private Timestamp createdAt;
    private Timestamp startedAt;
    private Timestamp finishedAt;


    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public WorkOrderStatus getStatus() {
        return status;
    }

    public void setStatus(WorkOrderStatus status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Timestamp startedAt) {
        this.startedAt = startedAt;
    }

    public Timestamp getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(Timestamp finishedAt) {
        this.finishedAt = finishedAt;
    }
}
