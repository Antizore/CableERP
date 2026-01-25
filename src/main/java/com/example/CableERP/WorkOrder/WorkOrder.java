package com.example.CableERP.WorkOrder;

import com.example.CableERP.Product.Product;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.sql.Timestamp;

@jakarta.persistence.Entity
@Table(name = "work_order")
public class WorkOrder {

    protected WorkOrder(){}

    public WorkOrder(Product workOrderProduct, Double qty, WorkOrderStatus status,Timestamp createdAt,
                     Timestamp plannedStartAt, Timestamp startedAt, Timestamp finishedAt, Timestamp plannedEndAt) {
        this.workOrderProduct = workOrderProduct;
        this.qty = qty;
        this.status = status;
        this.createdAt = createdAt;
        this.plannedStartAt = plannedStartAt;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
        this.plannedEndAt = plannedEndAt;
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
    private Timestamp plannedStartAt;
    private Timestamp startedAt;
    private Timestamp finishedAt;
    private Timestamp plannedEndAt;


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

    public Timestamp getPlannedStartAt() {
        return plannedStartAt;
    }

    public void setPlannedStartAt(Timestamp plannedStartAt) {
        this.plannedStartAt = plannedStartAt;
    }

    public Timestamp getPlannedEndAt() {
        return plannedEndAt;
    }

    public void setPlannedEndAt(Timestamp plannedEndAt) {
        this.plannedEndAt = plannedEndAt;
    }
}
