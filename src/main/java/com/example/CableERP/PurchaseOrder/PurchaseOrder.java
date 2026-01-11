package com.example.CableERP.PurchaseOrder;


import com.example.CableERP.Procurement.Procurement;
import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Procurement procurement;

    @Enumerated
    private PurchaseOrderStatus purchaseOrderStatus;

    private Timestamp createdAt;
    private Timestamp sent_at;
    private Timestamp received_at;


}
