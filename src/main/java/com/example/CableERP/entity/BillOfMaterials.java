package com.example.CableERP.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;



@Entity
@Table(name = "bill_of_material")
public class BillOfMaterials {

    protected BillOfMaterials(){}

    public BillOfMaterials(Long id, Product product, Component component, Double qty) {
        this.id = id;
        this.product = product;
        this.component = component;
        this.qty = qty;
    }

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonBackReference("product-bom")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "component_id")
    @JsonBackReference("component-bom")
    private Component component;

    private Double qty;


    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }
}
