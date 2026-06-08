package simpleerp.product;


import org.hibernate.annotations.BatchSize;
import simpleerp.bom.BillOfMaterials;
import simpleerp.customer.co.OrderItem;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@jakarta.persistence.Entity
@Table(name = "product")
public class Product {

    protected Product() {
    }


    //TODO: add minutesToProduce and update across different functions
    public Product(String name, String description) {
        this.name = name;
        this.description = description;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "minutes_to_produce", nullable = false)
    private BigDecimal minutesToProduceOnePiece;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @BatchSize(size = 50)
    private List<BillOfMaterials> billOfMaterialsList = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<OrderItem> orderItemList = new ArrayList<>();


    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public BigDecimal getMinutesToProduceOnePiece() {
        return minutesToProduceOnePiece;
    }
    public List<BillOfMaterials> getBillOfMaterialsList() {
        return billOfMaterialsList;
    }
    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setMinutesToProduceOnePiece(BigDecimal minutesToProduceOnePiece) {
        this.minutesToProduceOnePiece = minutesToProduceOnePiece;}
    public void setBillOfMaterialsList(List<BillOfMaterials> billOfMaterialsList) {
        this.billOfMaterialsList = billOfMaterialsList;
    }
    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

}


