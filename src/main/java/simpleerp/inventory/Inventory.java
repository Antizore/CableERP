package simpleerp.inventory;

import simpleerp.component.Component;
import jakarta.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "inventory_item")
public class Inventory {

    protected Inventory() {}

    public Inventory(Component component, double qtyAvailable, double qtyReserved) {
        this.component = component;
        this.qtyAvailable = qtyAvailable;
        this.qtyReserved = qtyReserved;
        this.updatedAt = Timestamp.from(Instant.now());
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "component_id", unique = true)
    private Component component;

    @Column(name = "qty_available")
    private double qtyAvailable;

    @Column(name = "qty_reserved")
    private double qtyReserved;

    @Column(name = "updated_at")
    private Timestamp updatedAt;


    public Long getId() { return id; }
    public Component getComponent() { return component; }
    public double getQtyAvailable() { return qtyAvailable; }
    public void setQtyAvailable(double qtyAvailable) { this.qtyAvailable = qtyAvailable; }
    public double getQtyReserved() { return qtyReserved; }
    public void setQtyReserved(double qtyReserved) { this.qtyReserved = qtyReserved; }
    public Timestamp getUpdatedAt() {return updatedAt;}
    public void setUpdatedAt(Timestamp updatedAt) {this.updatedAt = updatedAt;}
}