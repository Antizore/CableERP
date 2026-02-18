package simpleerp.po;

import simpleerp.vendor.Vendor;
import simpleerp.customer.co.Order;
import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "purchase_order")
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "linked_customer_order_id")
    private Order linkedCustomerOrder;

    @Enumerated(EnumType.STRING)
    private PurchaseOrderStatus status;

    @Column(name = "expected_delivery_at")
    private Timestamp expectedDeliveryAt;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "received_at")
    private Timestamp receivedAt;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL)
    private List<PurchaseOrderItem> items = new ArrayList<>();

    // Getters Setters
    public void setVendor(Vendor vendor) { this.vendor = vendor; }
    public void setLinkedCustomerOrder(Order order) { this.linkedCustomerOrder = order; }
    public Timestamp getExpectedDeliveryAt() { return expectedDeliveryAt; }
    public void setExpectedDeliveryAt(Timestamp expectedDeliveryAt) { this.expectedDeliveryAt = expectedDeliveryAt; }
}



