package simpleerp.Component;


import simpleerp.BillOfMaterials.BillOfMaterials;
import simpleerp.Inventory.Inventory;
import simpleerp.Reservation.Reservation;
import simpleerp.Common.Unit;
import simpleerp.Vendor.ComponentVendor;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "component")
public class Component {

    protected Component(){}
    public Component(String name, Unit unit, Double costPerUnit) {
        this.name = name;
        this.unit = unit;
        this.costPerUnit = costPerUnit;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column (name = "unit", nullable = false)
    @Enumerated(EnumType.STRING)
    private Unit unit;

    @Column(name = "cost_per_unit", nullable = false, precision = 2)
    private Double costPerUnit;

    @OneToMany(mappedBy = "component")
    private List<BillOfMaterials> billOfMaterialsList = new ArrayList<>();

    @OneToOne(mappedBy = "component")
    private Inventory inventory;

    @OneToMany(mappedBy = "component")
    private List<Reservation> reservationList;

    @OneToMany(mappedBy = "component", cascade = CascadeType.ALL)
    private List<ComponentVendor> componentVendors = new ArrayList<>();



    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Unit getUnit() { return unit; }
    public void setUnit(Unit unit) { this.unit = unit; }
    public Double getCostPerUnit() { return costPerUnit; }
    public void setCostPerUnit(Double costPerUnit) { this.costPerUnit = costPerUnit; }
    public List<ComponentVendor> getComponentVendors() { return componentVendors; }
    public Inventory getInventory() { return inventory; }
    public void setInventory(Inventory inventory) { this.inventory = inventory; }

}
