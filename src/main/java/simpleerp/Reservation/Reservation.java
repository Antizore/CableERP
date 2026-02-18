package simpleerp.Reservation;

import simpleerp.component.Component;
import simpleerp.Customer.CustomerOrder.Order;
import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "stock_reservation")
public class Reservation {

    protected Reservation() {}

    public Reservation(Order order, Component component, double qty) {
        this.customerOrder = order;
        this.component = component;
        this.qty = qty;
        this.isFulfilled = false;
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_order_id")
    private Order customerOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "component_id", nullable = false)
    private Component component;

    private double qty;

    @Column(name = "is_fulfilled")
    private boolean isFulfilled = false;

    @Column(name = "created_at")
    private Timestamp createdAt;

    // Getters and Setters
    public Long getId() { return id; }
    public boolean isFulfilled() { return isFulfilled; }
    public void setFulfilled(boolean fulfilled) { isFulfilled = fulfilled; }
    public double getQty() { return qty; }
    public void setQty(double qty) { this.qty = qty; }
    public Component getComponent() { return component; }
    public Order getCustomerOrder() {return customerOrder;}
}