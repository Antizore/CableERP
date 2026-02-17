package simpleerp.Customer.CustomerOrder;

import simpleerp.Customer.Customer;
import simpleerp.PurchaseOrder.PurchaseOrder;
import simpleerp.Reservation.Reservation;
import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customer_order")
public class Order {

    protected Order() {}

    public Order(Customer customer, OrderStatus status) {
        this.customer = customer;
        this.status = status;
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "planned_start_at")
    private Timestamp plannedStartAt;

    @Column(name = "planned_end_at")
    private Timestamp plannedEndAt;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItemList = new ArrayList<>();

    @OneToMany(mappedBy = "customerOrder", cascade = CascadeType.ALL)
    private List<Reservation> reservations = new ArrayList<>();

    @OneToMany(mappedBy = "linkedCustomerOrder")
    private List<PurchaseOrder> linkedPurchaseOrders = new ArrayList<>();


    public Long getId() { return id; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public List<OrderItem> getOrderItemList() { return orderItemList; }
    public Timestamp getPlannedStartAt() { return plannedStartAt; }
    public Timestamp getPlannedEndAt() { return plannedEndAt; }
    public void setPlannedStartAt(Timestamp plannedStartAt) { this.plannedStartAt = plannedStartAt; }
    public void setPlannedEndAt(Timestamp plannedEndAt) { this.plannedEndAt = plannedEndAt; }
}

