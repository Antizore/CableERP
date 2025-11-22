package com.example.CableERP.WorkOrder;

import com.example.CableERP.BillOfMaterials.BillOfMaterials;
import com.example.CableERP.BillOfMaterials.BillOfMaterialsRepository;
import com.example.CableERP.Common.Exception.WrongValueException;
import com.example.CableERP.Inventory.Inventory;
import com.example.CableERP.Inventory.InventoryRepository;
import com.example.CableERP.Product.Product;
import com.example.CableERP.Product.ProductRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

@Service
public class WorkOrderService {

    private final WorkOrderRepository workOrderRepository;
    private final ProductRepository productRepository;
    private final BillOfMaterialsRepository billOfMaterialsRepository;
    private final InventoryRepository inventoryRepository;

    public WorkOrderService(WorkOrderRepository workOrderRepository, ProductRepository productRepository, BillOfMaterialsRepository billOfMaterialsRepository, InventoryRepository inventoryRepository){
        this.workOrderRepository = workOrderRepository;
        this.productRepository = productRepository;
        this.billOfMaterialsRepository = billOfMaterialsRepository;
        this.inventoryRepository = inventoryRepository;
    }


    public List<ReturnAllWorkOrdersDTO> getAllWorkOrders(){
        return workOrderRepository.findAll().stream().map(
                workOrder -> new ReturnAllWorkOrdersDTO(
                        workOrder.getId(),
                        workOrder.getProduct().getName(),
                        workOrder.getQty(),
                        workOrder.getStatus(),
                        workOrder.getCreatedAt(),
                        workOrder.getStartedAt(),
                        workOrder.getFinishedAt()
                )
        ).sorted((Comparator.comparing(ReturnAllWorkOrdersDTO::createdAt))).toList();
    }


    public CreateWorkOrderResponseDTO createWorkOrder(CreateWorkOrderRequestDTO createWorkOrderDTO){
        if(createWorkOrderDTO.qty() <= 0) throw  new WrongValueException("Qty cannot be less or equal 0");
        Calendar calendar =  Calendar.getInstance();
        Product product = productRepository.findById(createWorkOrderDTO.productId()).orElseThrow();
        WorkOrder workOrder = new WorkOrder(product, createWorkOrderDTO.qty(), WorkOrderStatus.PLANNED, new Timestamp(calendar.getTimeInMillis()));
        workOrderRepository.saveAndFlush(workOrder);
        return new CreateWorkOrderResponseDTO(workOrder.getId(),product.getId(),workOrder.getQty(), workOrder.getStatus());
    }

    public void startWorkOrder(Long id){
        WorkOrder workOrder = workOrderRepository.findById(id).orElseThrow();
        //TODO zrobić customowy exception w module WorkOrder
        if (workOrder.getStatus() != WorkOrderStatus.PLANNED) throw new WrongValueException("tu inny exception");

         List<BillOfMaterials> ListOfBills = billOfMaterialsRepository.findAllByProduct_Id(workOrder.getProduct().getId());
         List<Inventory> inventoryList = new ArrayList<>();

         for(BillOfMaterials bill: ListOfBills){
             Inventory componentInInventory = inventoryRepository.findByComponentId(bill.getComponent().getId());
             double qtyNeeded = bill.getQty() * workOrder.getQty();
             double qtyAvailable = componentInInventory.getQtyAvailable();
             if(qtyAvailable >= qtyNeeded){
                 componentInInventory.setQtyAvailable(qtyAvailable - qtyNeeded);
                 inventoryList.add(componentInInventory);
             }
             else throw new WrongValueException("tu inny exception");
         }
         inventoryRepository.saveAllAndFlush(inventoryList);
         workOrder.setStatus(WorkOrderStatus.IN_PROGRESS);
         Calendar calendar =  Calendar.getInstance();
         workOrder.setStartedAt(new Timestamp(calendar.getTimeInMillis()));
    }



    public void finishWorkOrder(Long id){
        WorkOrder workOrder = workOrderRepository.findById(id).orElseThrow();
        if(workOrder.getStatus() == WorkOrderStatus.IN_PROGRESS ){
            Calendar calendar = Calendar.getInstance();
            workOrder.setStatus(WorkOrderStatus.FINISHED);
            workOrder.setFinishedAt(new Timestamp(calendar.getTimeInMillis()));


            workOrderRepository.saveAndFlush(workOrder);

        }
        else throw new WrongValueException("tu inny exception");
    }

    public void calculateRequiredComponents(){

    }




}
