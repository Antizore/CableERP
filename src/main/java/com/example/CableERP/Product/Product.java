package com.example.CableERP.Product;


import com.example.CableERP.BillOfMaterials.BillOfMaterials;
import com.example.CableERP.Order.Order;
import com.example.CableERP.Order.OrderItem;
import com.example.CableERP.WorkOrder.WorkOrder;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@jakarta.persistence.Entity
@Table(name = "product")
public class Product {

    protected Product() {}


    public Product(String name, String description) {
        this.name = name;
        this.description = description;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<BillOfMaterials> billOfMaterialsList = new ArrayList<>();


    @OneToMany(mappedBy = "workOrderProduct", cascade = CascadeType.ALL)
    private List<WorkOrder> workOrderList = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<OrderItem> orderItemList = new ArrayList<>();


    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<BillOfMaterials> getBillOfMaterialsList() { return billOfMaterialsList; }
    public void setBillOfMaterialsList(List<BillOfMaterials> billOfMaterialsList) {
        this.billOfMaterialsList = billOfMaterialsList;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", billOfMaterialsList=" + billOfMaterialsList +
                '}';
    }
}


