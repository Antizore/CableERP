package simpleerp.inventory;

import simpleerp.component.Component;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "inventory_item")
public class Inventory {

    protected Inventory() {}

    public Inventory(Component component, BigDecimal qtyAvailable, BigDecimal qtyReserved) {
        this.component = component;
        this.qtyAvailable = qtyAvailable;
        this.qtyReserved = qtyReserved;
        this.updatedAt = Timestamp.from(Instant.now());
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "component_id", unique = true, nullable = false)
    private Component component;

    @Column(name = "qty_available", nullable = false, precision = 10)
    private BigDecimal qtyAvailable;

    @Column(name = "qty_reserved", nullable = false, precision = 10)
    private BigDecimal qtyReserved;

    @Column(name = "updated_at")
    private Timestamp updatedAt;


    public Long getId() { return id; }
    public Component getComponent() { return component; }
    public BigDecimal getQtyAvailable() { return qtyAvailable; }
    public BigDecimal getQtyReserved() { return qtyReserved; }
    public Timestamp getUpdatedAt() {return updatedAt;}

    public void setQtyAvailable(BigDecimal qtyAvailable) { this.qtyAvailable = qtyAvailable; }
    public void setQtyReserved(BigDecimal qtyReserved) { this.qtyReserved = qtyReserved; }
    public void setUpdatedAt(Timestamp updatedAt) {this.updatedAt = updatedAt;}
}