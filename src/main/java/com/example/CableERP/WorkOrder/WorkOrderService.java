package com.example.CableERP.WorkOrder;

import com.example.CableERP.Common.Exception.MissingEntityException;
import com.example.CableERP.Common.Exception.WrongValueException;
import com.example.CableERP.Product.Product;
import com.example.CableERP.Product.ProductRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;

@Service
public class WorkOrderService {

    private final WorkOrderRepository workOrderRepository;
    private final ProductRepository productRepository;

    public WorkOrderService(WorkOrderRepository workOrderRepository, ProductRepository productRepository){
        this.workOrderRepository = workOrderRepository;
        this.productRepository = productRepository;
    }



    public CreateWorkOrderResponseDTO createWorkOrder(CreateWorkOrderRequestDTO createWorkOrderDTO){
        if(createWorkOrderDTO.qty() <= 0) throw  new WrongValueException("Qty cannot be less or equal 0");
        Calendar calendar =  Calendar.getInstance();
        Product product = productRepository.findById(createWorkOrderDTO.productId()).orElseThrow();
        WorkOrder workOrder = new WorkOrder(product, createWorkOrderDTO.qty(), WorkOrderStatus.PLANNED, new Timestamp(calendar.getTimeInMillis()));
        workOrderRepository.saveAndFlush(workOrder);
        return new CreateWorkOrderResponseDTO(workOrder.getId(),product.getId(),workOrder.getQty(), workOrder.getStatus());
    }

    public void startWorkOrder(){

    }

    public void finishWorkOrder(){

    }

    public void calculateRequiredComponents(){

    }




}
