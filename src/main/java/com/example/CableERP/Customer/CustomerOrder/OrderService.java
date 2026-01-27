package com.example.CableERP.Customer.CustomerOrder;


import com.example.CableERP.Common.Exception.IllegalOperationException;
import com.example.CableERP.Common.Exception.WrongValueException;
import com.example.CableERP.Customer.CustomerRepository;
import com.example.CableERP.Inventory.InventoryRepository;
import com.example.CableERP.MRP.Estimation;
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
    private final Estimation estimation;


    public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository,
                        ProductRepository productRepository, OrderItemRepository orderItemRepository,
                        InventoryRepository inventoryRepository, Estimation estimation) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
        this.estimation = estimation;
    }

    public List<ShowOrderDTO> returnAllOrders() {
        List<ShowOrderDTO> orders = new ArrayList<>();



        for (Order order : orderRepository.findAll()) {
            List<OrderItemDTO> orderItemDTOList = new ArrayList<>();
            List<OrderItem> aa = orderItemRepository.findAllByOrderId(order.getId());
            for (OrderItem order1 : aa) {
                orderItemDTOList.add(new OrderItemDTO(order1.getId(), new ProductCreateDTO(order1.getProduct().getName(), order1.getProduct().getDescription()), order1.getQty()));
            }

            orders.add(
                    new ShowOrderDTO(order, orderItemDTOList)
            );
        }
        return orders;
    }

    public ShowOrderDTO returnOrderById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow();
        List<OrderItemDTO> orderItemDTOList = new ArrayList<>();
        List<OrderItem> aa = orderItemRepository.findAllByOrderId(order.getId());
        for (OrderItem order1 : aa) {
            orderItemDTOList.add(new OrderItemDTO(order1.getId(), new ProductCreateDTO(order1.getProduct().getName(), order1.getProduct().getDescription()), order1.getQty()));
        }

        return new ShowOrderDTO(order, orderItemDTOList);
    }

    public Order saveOrderToDB(@NonNull List<CreateItemsInOrderDTO> createItemsInOrderDTOList, Long customerId) {
        Calendar rightNow = Calendar.getInstance();

        Order order = new Order(
                customerRepository.findById(customerId).orElseThrow(),
                OrderStatus.NEW,
                new Timestamp(rightNow.getTimeInMillis()),
                new Timestamp(rightNow.getTimeInMillis())
        );
        Order saved = orderRepository.saveAndFlush(order);
        addItemsToOrder(saved.getId(), createItemsInOrderDTOList);

        List<OrderItem> orderItems = orderItemRepository.findAllByOrderId(saved.getId());

        saved.setPlannedStartAt(estimation.estimate(orderItems).getFirst());
        saved.setPlannedEndAt(estimation.estimate(orderItems).getLast());

        return orderRepository.saveAndFlush(saved);
    }


    public void addItemsToOrder(Long orderId, @NonNull List<CreateItemsInOrderDTO> createItemsInOrderDTOList) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        List<OrderItem> currentItems = orderItemRepository.findAllByOrderId(orderId);
        List<OrderItem> orderItemList = new ArrayList<>();
        OrderStatus[] blockedStatuses = {OrderStatus.CANCELLED, OrderStatus.COMPLETED, OrderStatus.IN_PRODUCTION};
        if (Arrays.stream(blockedStatuses).toList().contains(order.getStatus()))
            throw new IllegalOperationException("Cannot add items to this order because of its status: " + order.getStatus());


        Map<Long, OrderItem> currentItemsMap = currentItems.stream().collect(Collectors.toMap(orderItem -> orderItem.getProduct().getId(), Function.identity()));


        for (CreateItemsInOrderDTO item : createItemsInOrderDTOList) {

            Double qty;
            if (currentItemsMap.containsKey(item.productId())) {
                qty = currentItemsMap.get(item.productId()).getQty() + item.qty();
            } else qty = item.qty();


            orderItemList.add(
                    new OrderItem(
                            order,
                            productRepository.findById(item.productId()).orElseThrow(),
                            qty
                    )
            );
        }
        //that's some lazy work...
        orderItemRepository.deleteAllByOrderId(orderId);
        orderItemRepository.saveAllAndFlush(orderItemList);
    }


    public void deleteItemsFromOrder(Long orderId, Long id){
        Order order = orderRepository.findById(orderId).orElseThrow();
        OrderStatus[] blockedStatuses = {OrderStatus.CANCELLED, OrderStatus.COMPLETED, OrderStatus.IN_PRODUCTION};
        if (Arrays.stream(blockedStatuses).toList().contains(order.getStatus()))
            throw new IllegalOperationException("Cannot delete items from this order because of its status: " + order.getStatus());
        if(id == null) orderItemRepository.deleteAllByOrderId(orderId);
        else orderItemRepository.deleteById(id);
    }

    public void updateItemInOrder(Long orderId, Long id, Double qty) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        OrderStatus[] blockedStatuses = {OrderStatus.CANCELLED, OrderStatus.COMPLETED, OrderStatus.IN_PRODUCTION};
        if (Arrays.stream(blockedStatuses).toList().contains(order.getStatus()))
            throw new IllegalOperationException("Cannot change items in this order because of its status: " + order.getStatus());

        OrderItem orderItem = orderItemRepository.findById(id).orElseThrow();
        if(qty.isNaN() || qty==0 || qty < 0) throw new WrongValueException("Did you mean delete?");
        else orderItem.setQty(qty);
    }


}
