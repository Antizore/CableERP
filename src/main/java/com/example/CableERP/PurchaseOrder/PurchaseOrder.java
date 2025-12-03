package com.example.CableERP.PurchaseOrder;


import com.example.CableERP.Vendor.Vendor;
import jakarta.persistence.*;

import java.sql.Time;
import java.sql.Timestamp;

@Entity
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Vendor vendor;

    @Enumerated
    private PurchaseOrderStatus purchaseOrderStatus;

    private Timestamp createdAt;
    private Timestamp sent_at;
    private Timestamp received_at;


}
