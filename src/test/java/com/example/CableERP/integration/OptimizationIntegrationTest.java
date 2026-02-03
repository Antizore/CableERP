package com.example.CableERP.integration;

import com.example.CableERP.Customer.Customer;
import com.example.CableERP.Customer.CustomerOrder.*;
import com.example.CableERP.Customer.CustomerRepository;
import com.example.CableERP.MRP.OptimizationService;
import com.example.CableERP.Notification.Notification;
import com.example.CableERP.Notification.NotificationRepository;
import com.example.CableERP.Product.Product;
import com.example.CableERP.Product.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class OptimizationIntegrationTest {

    @Autowired OrderRepository orderRepository;
    @Autowired OptimizationService optimizationService;
    @Autowired NotificationRepository notificationRepository;
    @Autowired CustomerRepository customerRepository;
    @Autowired ProductRepository productRepository;

    @Test
    @Transactional
    void shouldCreateNotificationInDB_WhenOptimizationIsPossible() {
        // 1. SETUP DANYCH
        Customer customer = customerRepository.save(new Customer("Opti Client", "123", "a@b.com"));
        Product product = new Product("Fast Cable", "Desc");
        product.setMinutesToProduceOnePiece(10.0); // Bardzo szybka produkcja
        product = productRepository.save(product);

        // 2. KOREK: Tworzymy zamówienie A, które blokuje kolejkę (startuje za 7 dni)
        Order blocker = new Order(customer, OrderStatus.WAITING_FOR_COMPONENTS);
        blocker.setPlannedStartAt(Timestamp.from(Instant.now().plus(7, ChronoUnit.DAYS)));
        orderRepository.save(blocker);

        // 3. KANDYDAT: Tworzymy zamówienie B, które jest READY i startuje po blockerze
        // (Symulujemy, że EstimationService wrzucił je na koniec kolejki)
        Order candidate = new Order(customer, OrderStatus.READY_FOR_PRODUCTION);
        candidate.setPlannedStartAt(Timestamp.from(Instant.now().plus(8, ChronoUnit.DAYS)));

        // Dodajemy item, żeby system wiedział ile trwa produkcja (10 min)
        OrderItem item = new OrderItem(candidate, product, 1.0);
        candidate.getOrderItemList().add(item);

        candidate = orderRepository.save(candidate);

        // 4. AKCJA: Uruchamiamy ręcznie optymalizację dla kandydata
        // (Normalnie robi to OrderService, ale tu testujemy sam mechanizm zapisu alertu)
        optimizationService.checkForOptimization(candidate);

        // 5. WERYFIKACJA: Czy alert trafił do bazy?
        List<Notification> alerts = notificationRepository.findAll();

        assertFalse(alerts.isEmpty(), "Powinien pojawić się przynajmniej jeden alert");

        Long candidateId = candidate.getId();

        Notification alert = alerts.stream().filter(
                a -> a.getMessage() != null && a.getMessage().contains("Order #" + candidateId)).findFirst().orElseThrow(() -> new AssertionError("Cant find"));

        System.out.println("ALERT MESSAGE: " + alert.getMessage()); // Dla podglądu

        assertTrue(alert.getMessage().contains("OPTIMIZATION"), "Wiadomość powinna dotyczyć optymalizacji");
        assertTrue(alert.getMessage().contains("Order #" + candidate.getId()), "Wiadomość powinna wspominać ID kandydata");
    }
}