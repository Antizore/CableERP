package com.example.CableERP.WorkOrder;

import com.example.CableERP.Product.Product;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.sql.Timestamp;

@jakarta.persistence.Entity
@Table(name = "work_order")
public class WorkOrder {

    protected WorkOrder(){}

    public WorkOrder(Product workOrderProduct, Double qty, WorkOrderStatus status, Timestamp createdAt) {
        this.workOrderProduct = workOrderProduct;
        this.qty = qty;
        this.status = status;
        this.createdAt = createdAt;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product workOrderProduct;
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
        return workOrderProduct;
    }

    public void setProduct(Product product) {
        this.workOrderProduct = product;
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
