package com.example.CableERP.Customer.CustomerOrder;


import com.example.CableERP.Customer.CustomerRepository;
import com.example.CableERP.Product.ProductCreateDTO;
import com.example.CableERP.Product.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.sql.Timestamp;

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

    public Order returnOrderById(Long id){
        return orderRepository.findById(id).orElseThrow();
    }

    public Order saveOrderToDB(CreateOrderDTO customerOrderDTO){
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


    public void addItemsToOrder(Long orderId,List<CreateItemsInOrderDTO> createItemsInOrderDTOList){
        Order order = orderRepository.findById(orderId).orElseThrow();

        List<OrderItem> orderItemList = new ArrayList<>();
        for(CreateItemsInOrderDTO item : createItemsInOrderDTOList){
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
