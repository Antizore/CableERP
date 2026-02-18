package simpleerp.bom;

import simpleerp.component.Component;
import simpleerp.product.Product;
import jakarta.persistence.*;



@Entity
@Table(name = "bill_of_material")
public class BillOfMaterials {

    protected BillOfMaterials(){}

    public BillOfMaterials(Product product, Component component, double qty) {
        this.product = product;
        this.component = component;
        this.qty = qty;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "component_id")
    private Component component;

    @Column(nullable = false, precision = 10)
    private double qty;


    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }
}
