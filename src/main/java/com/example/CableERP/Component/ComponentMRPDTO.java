package com.example.CableERP.Component;

public class ComponentMRPDTO  {

    public ComponentMRPDTO(Long id, String name, Double qty) {
        this.id = id;
        this.name = name;
        this.qty = qty;
    }

    private Long id;
    private String name;
    private Double qty;


    public Long getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }


}
