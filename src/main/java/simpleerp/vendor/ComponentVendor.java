package simpleerp.vendor;

import simpleerp.component.Component;
import jakarta.persistence.*;

@Entity
@Table(name = "component_vendor", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"component_id", "vendor_id"})
})
public class ComponentVendor {

    protected ComponentVendor() {}

    public ComponentVendor(Component component, Vendor vendor, int leadTimeDays, double price) {
        this.component = component;
        this.vendor = vendor;
        this.leadTimeDays = leadTimeDays;
        this.price = price;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "component_id", nullable = false)
    private Component component;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    @Column(name = "lead_time_days", nullable = false)
    private int leadTimeDays; // KLUCZOWE DLA ALGORYTMU

    @Column(nullable = false)
    private double price;

    @Column(name = "is_preferred")
    private boolean isPreferred = false;

    // Getters and Setters
    public Long getId() { return id; }
    public Component getComponent() { return component; }
    public void setComponent(Component component) { this.component = component; }
    public Vendor getVendor() { return vendor; }
    public void setVendor(Vendor vendor) { this.vendor = vendor; }
    public int getLeadTimeDays() { return leadTimeDays; }
    public void setLeadTimeDays(int leadTimeDays) { this.leadTimeDays = leadTimeDays; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public boolean isPreferred() { return isPreferred; }
    public void setPreferred(boolean preferred) { isPreferred = preferred; }
}