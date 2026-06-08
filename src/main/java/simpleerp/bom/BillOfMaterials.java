package simpleerp.bom;

import simpleerp.component.Component;
import simpleerp.product.Product;
import jakarta.persistence.*;

import java.math.BigDecimal;


@Entity
@Table(name = "bill_of_material")
public class BillOfMaterials {

    protected BillOfMaterials(){}

    public BillOfMaterials(Product product, Component component, BigDecimal qty) {
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
    private BigDecimal qty;


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

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }
}
