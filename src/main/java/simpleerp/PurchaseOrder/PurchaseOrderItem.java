package simpleerp.PurchaseOrder;


import simpleerp.Component.Component;
import jakarta.persistence.*;

@Entity
public class PurchaseOrderItem {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private PurchaseOrder purchaseOrder;

    @OneToOne
    private Component component;

    private Double qty;

}
