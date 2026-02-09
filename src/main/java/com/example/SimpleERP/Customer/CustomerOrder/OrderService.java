package com.example.SimpleERP.Customer.CustomerOrder;

import com.example.SimpleERP.BillOfMaterials.BillOfMaterials;
import com.example.SimpleERP.Common.Exception.IllegalOperationException;
import com.example.SimpleERP.Customer.CustomerRepository;
import com.example.SimpleERP.MRP.EstimationService;
import com.example.SimpleERP.MRP.OptimizationService;
import com.example.SimpleERP.Product.Product;
import com.example.SimpleERP.Product.ProductRepository;
import com.example.SimpleERP.Product.ProductCreateDTO;
import com.example.SimpleERP.Reservation.ReservationRepository;
import com.example.SimpleERP.Reservation.ReservationRequestDTO;
import com.example.SimpleERP.Reservation.ReservationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final EstimationService estimationService;
    private final ReservationService reservationService;
    private final ReservationRepository reservationRepository;
    private final OptimizationService optimizationService;

    public OrderService(OrderRepository orderRepository,
                        CustomerRepository customerRepository,
                        ProductRepository productRepository,
                        OrderItemRepository orderItemRepository,
                        EstimationService estimationService,
                        ReservationService reservationService,
                        ReservationRepository reservationRepository, OptimizationService optimizationService) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
        this.estimationService = estimationService;
        this.reservationService = reservationService;
        this.reservationRepository = reservationRepository;
        this.optimizationService = optimizationService;
    }


    public List<ShowOrderDTO> returnAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public ShowOrderDTO returnOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found: " + id));
        return mapToDTO(order);
    }


    @Transactional
    public Order placeOrder(Long customerId, List<CreateItemsInOrderDTO> itemsDto) {
        Order order = new Order(
                customerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Customer not found")),
                OrderStatus.NEW
        );


        order = orderRepository.saveAndFlush(order);


        List<OrderItem> orderItems = new ArrayList<>();
        for (CreateItemsInOrderDTO dto : itemsDto) {
            Product product = productRepository.findById(dto.productId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + dto.productId()));

            OrderItem item = new OrderItem(order, product, dto.qty());
            orderItems.add(item);
        }
        orderItemRepository.saveAllAndFlush(orderItems);


        Map<Long,Double> mapOfComponentIdAndQtyNeeded = new HashMap<>();
        for (OrderItem item : orderItems) {
            Product product = item.getProduct();
            double productQty = item.getQty();

            for (BillOfMaterials bom : product.getBillOfMaterialsList()) {
                double componentQtyNeeded = bom.getQty() * productQty;

                /*
                mapOfComponentIdAndQtyNeeded.computeIfPresent(
                        bom.getComponent().getId(), (key, val) -> val + componentQtyNeeded);

                mapOfComponentIdAndQtyNeeded.computeIfAbsent(
                        bom.getComponent().getId(), k -> componentQtyNeeded);

                 */

                mapOfComponentIdAndQtyNeeded.merge(bom.getComponent().getId(),componentQtyNeeded,Double::sum);

            }
        }


        Long orderId = order.getId();
        mapOfComponentIdAndQtyNeeded.forEach((componentId,componentQtyNeeded) -> {
                ReservationRequestDTO request = new ReservationRequestDTO(
                        orderId,
                        componentId,
                        componentQtyNeeded
                );
                reservationService.makeReservation(request);}
        );



        long missingComponentsCount = reservationRepository.countByCustomerOrderIdAndIsFulfilledFalse(order.getId());
        if (missingComponentsCount == 0) {
            order.setStatus(OrderStatus.READY_FOR_PRODUCTION);
        } else {
            order.setStatus(OrderStatus.WAITING_FOR_COMPONENTS);
        }
        List<Timestamp> schedule = estimationService.estimate(orderItems);
        order.setPlannedStartAt(schedule.get(0));
        order.setPlannedEndAt(schedule.get(1));

        Order savedOrder = orderRepository.saveAndFlush(order);

        optimizationService.checkForOptimization(savedOrder);
        return savedOrder;
    }

    @Transactional
    public void tryPromoteOrderToReady(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        if (order.getStatus() != OrderStatus.WAITING_FOR_COMPONENTS) {
            return;
        }
        long missingCount = reservationRepository.countByCustomerOrderIdAndIsFulfilledFalse(orderId);
        if (missingCount == 0) {
            order.setStatus(OrderStatus.READY_FOR_PRODUCTION);
            orderRepository.saveAndFlush(order);
            optimizationService.checkForOptimization(order);
        }
    }

    @Transactional
    public void deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        if (order.getStatus() == OrderStatus.IN_PRODUCTION || order.getStatus() == OrderStatus.COMPLETED) {
            throw new IllegalOperationException("Cannot delete order in progress/completed");
        }
        orderRepository.delete(order);
    }

    private ShowOrderDTO mapToDTO(Order order) {
        List<OrderItemDTO> items = order.getOrderItemList().stream()
                .map(i -> new OrderItemDTO(
                        i.getId(),
                        new ProductCreateDTO(i.getProduct().getName(), i.getProduct().getDescription()),
                        i.getQty()))
                .collect(Collectors.toList());
        return new ShowOrderDTO(order, items);
    }
}