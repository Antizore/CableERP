package com.example.CableERP.Customer.CustomerOrder;


import com.example.CableERP.Common.Exception.IllegalOperationException;
import com.example.CableERP.Customer.CustomerRepository;
import com.example.CableERP.Product.ProductCreateDTO;
import com.example.CableERP.Product.ProductRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.*;
import java.sql.Timestamp;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository, ProductRepository productRepository, OrderItemRepository orderItemRepository){
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public List<ShowOrderDTO> returnAllOrders(){
        List<ShowOrderDTO> orders = new ArrayList<>();


        /*

            id BIGSERIAL PRIMARY KEY,
            order_id BIGINT REFERENCES customer_order(id) ON DELETE CASCADE,
            product_id BIGINT REFERENCES product(id),
            qty NUMERIC(10,2) NOT NULL

         */

        for(Order order : orderRepository.findAll()){
            List<OrderItemDTO> orderItemDTOList = new ArrayList<>();
            List<OrderItem> aa = orderItemRepository.findAllByOrderId(order.getId());
            for(OrderItem order1 : aa){
                orderItemDTOList.add(new OrderItemDTO(new ProductCreateDTO(order1.getProduct().getName(),order1.getProduct().getDescription()),order1.getQty()));
            }

            orders.add(
                    new ShowOrderDTO(order, orderItemDTOList)
            );
        }
        return orders;
    }

    public ShowOrderDTO returnOrderById(Long id){
        Order order = orderRepository.findById(id).orElseThrow();

        List<OrderItemDTO> orderItemDTOList = new ArrayList<>();
        List<OrderItem> aa = orderItemRepository.findAllByOrderId(order.getId());

        for(OrderItem order1 : aa){
            orderItemDTOList.add(new OrderItemDTO(new ProductCreateDTO(order1.getProduct().getName(),order1.getProduct().getDescription()),order1.getQty()));
        }


        return new ShowOrderDTO(order, orderItemDTOList);

    }

    public Order saveOrderToDB(@NonNull CreateOrderDTO customerOrderDTO){
        Calendar rightNow = Calendar.getInstance();

        Order order = new Order(
                customerRepository.findById(customerOrderDTO.customerId()).orElseThrow(),
                customerOrderDTO.orderNumber(),
                OrderStatus.NEW,
                new Timestamp(rightNow.getTimeInMillis()),
                new Timestamp(rightNow.getTimeInMillis())
        );

        return orderRepository.saveAndFlush(order);
    }


    public void addItemsToOrder(Long orderId, @NonNull List<CreateItemsInOrderDTO> createItemsInOrderDTOList){
        Order order = orderRepository.findById(orderId).orElseThrow();
        List<OrderItem> currentItems = orderItemRepository.findAllByOrderId(orderId);
        List<OrderItem> orderItemList = new ArrayList<>();
        OrderStatus[] blockedStatuses = {OrderStatus.CANCELLED, OrderStatus.COMPLETED, OrderStatus.IN_PRODUCTION};
        if(Arrays.stream(blockedStatuses).toList().contains(order.getOrderStatus())) throw new IllegalOperationException("Cannot add items to this order because of its status: "+order.getOrderStatus());


        // zmapować na <Long, OrderItem> aby móc porównywać
        Map<Long, OrderItem> currentItemsMap = currentItems.stream().collect(Collectors.toMap(orderItem -> orderItem.getProduct().getId(), Function.identity()));


        for(CreateItemsInOrderDTO item : createItemsInOrderDTOList){

            if(currentItemsMap.containsKey(item.productId())) {

            }


            orderItemList.add(
                    new OrderItem(
                            order,
                            productRepository.findById(item.productId()).orElseThrow(),
                            item.qty()
                    )
            );
        }
        orderItemRepository.saveAllAndFlush(orderItemList);
    }



}
