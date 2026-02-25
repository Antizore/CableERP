package simpleerp.po;


import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;

    public PurchaseOrderService(PurchaseOrderRepository purchaseOrderRepository){
        this.purchaseOrderRepository = purchaseOrderRepository;
    }


    @Transactional
    public void orderPurchase(PurchaseOrder purchaseOrder){
        purchaseOrderRepository.saveAndFlush(purchaseOrder);
    }




}
