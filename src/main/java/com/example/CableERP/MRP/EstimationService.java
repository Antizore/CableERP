package com.example.CableERP.MRP;

import com.example.CableERP.BillOfMaterials.BillOfMaterials;
import com.example.CableERP.Component.Component;
import com.example.CableERP.Vendor.ComponentVendor;
import com.example.CableERP.Customer.CustomerOrder.Order;
import com.example.CableERP.Customer.CustomerOrder.OrderItem;
import com.example.CableERP.Customer.CustomerOrder.OrderRepository;
import com.example.CableERP.Inventory.Inventory;
import com.example.CableERP.Inventory.InventoryRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EstimationService {

    private final InventoryRepository inventoryRepository;
    private final OrderRepository orderRepository;

    public EstimationService(InventoryRepository inventoryRepository, OrderRepository orderRepository) {
        this.inventoryRepository = inventoryRepository;
        this.orderRepository = orderRepository;
    }

    /**
     * Główna metoda estymująca.
     * Zwraca listę [Start, Koniec].
     */
    public List<Timestamp> estimate(List<OrderItem> orderItemList) {
        // 1. Kiedy maszyna będzie wolna?
        Timestamp machineFreeAt = getMachineAvailability();

        // 2. Ile musimy czekać na materiały? (w dniach)
        double materialDelayDays = calculateMaxMaterialDelay(orderItemList);

        // 3. Wylicz Start = MAX(Maszyna, Teraz + Materiały)
        Timestamp plannedStart = calculateStartDate(machineFreeAt, materialDelayDays);

        // 4. Wylicz Koniec = Start + Czas Produkcji
        Timestamp plannedEnd = calculateEndDate(plannedStart, orderItemList);

        return List.of(plannedStart, plannedEnd);
    }

    // ==========================================
    // LOGIKA MATERIAŁOWA (MRP LITE)
    // ==========================================

    private Double calculateMaxMaterialDelay(List<OrderItem> orderItemList) {
        // Krok A: Agregacja potrzeb (Jaki komponent -> Ile łącznie sztuk)
        Map<Component, Double> totalNeeds = new HashMap<>();

        for (OrderItem item : orderItemList) {
            double productQty = item.getQty();
            for (BillOfMaterials bom : item.getProduct().getBillOfMaterialsList()) {
                totalNeeds.merge(bom.getComponent(), bom.getQty() * productQty, Double::sum);
            }
        }

        // Krok B: Sprawdzenie dostępności i Lead Time
        // Szukamy "najwąstszego gardła" - czyli komponentu, na który będziemy czekać najdłużej
        double maxDelayDays = 0.0;

        for (Map.Entry<Component, Double> entry : totalNeeds.entrySet()) {
            Component component = entry.getKey();
            Double neededQty = entry.getValue();

            // Pobieramy Inventory (lub tworzymy wirtualne 0, jeśli brak rekordu)
            Inventory inventory = component.getInventory();
            double available = (inventory != null) ? inventory.getQtyAvailable() : 0.0;
            double reserved = (inventory != null) ? inventory.getQtyReserved() : 0.0;

            // NOWY WZÓR: Wolne = Fizyczne - Zarezerwowane
            double freeToUse = available - reserved;

            if (neededQty > freeToUse) {
                // Brakuje towaru. Musimy kupić. Ile to potrwa?
                int leadTime = getLeadTimeForComponent(component);
                if (leadTime > maxDelayDays) {
                    maxDelayDays = leadTime;
                }
            }
        }

        return maxDelayDays;
    }

    private int getLeadTimeForComponent(Component component) {
        List<ComponentVendor> vendors = component.getComponentVendors();

        if (vendors == null || vendors.isEmpty()) {
            // Sytuacja awaryjna: komponent nie ma dostawcy.
            // Dla symulacji przyjmijmy 0 lub rzućmy wyjątek.
            // W ERP to powinien być błąd konfiguracji.
            // throw new RuntimeException("Missing Vendor for component: " + component.getName());
            return 0; // Fallback
        }

        // Strategia: Szukamy dostawcy oznaczonego jako "preferred"
        return vendors.stream()
                .filter(ComponentVendor::isPreferred)
                .findFirst()
                .map(ComponentVendor::getLeadTimeDays)
                // Jeśli żaden nie jest preferred, bierzemy najdłuższy czas (bezpiecznik) lub pierwszy z brzegu
                .orElse(vendors.stream()
                        .mapToInt(ComponentVendor::getLeadTimeDays)
                        .max()
                        .orElse(0));
    }

    // ==========================================
    // LOGIKA MASZYNOWA (SCHEDULING)
    // ==========================================

    private Timestamp getMachineAvailability() {
        // Pobieramy datę zakończenia OSTATNIEGO zaplanowanego zlecenia
        return orderRepository.findFirstByPlannedEndAtIsNotNullOrderByPlannedEndAtDesc()
                .map(Order::getPlannedEndAt)
                // Jeśli baza jest pusta (pierwsze zlecenie ever), maszyna jest wolna "Teraz"
                .orElse(Timestamp.from(Instant.now()));
    }

    // ==========================================
    // LOGIKA DAT (MATEMATYKA)
    // ==========================================

    private Timestamp calculateStartDate(Timestamp machineFreeAt, double delayDays) {
        Instant now = Instant.now();

        // Kiedy materiały dojadą? (Teraz + LeadTime)
        // Zamieniamy dni (double) na minuty dla precyzji
        long delayMinutes = (long) (delayDays * 24 * 60);
        Instant materialsReadyAt = now.plus(delayMinutes, ChronoUnit.MINUTES);

        // Maszyna musi być wolna
        Instant machineReadyAt = machineFreeAt.toInstant();

        // Start = Późniejsza z tych dwóch dat
        if (materialsReadyAt.isAfter(machineReadyAt)) {
            return Timestamp.from(materialsReadyAt);
        } else {
            // Jeśli materiały są szybciej niż maszyna, musimy i tak czekać na maszynę.
            // Ale uwaga: Jeśli maszyna była wolna "wczoraj" (bo nic nie robi), a startujemy "dziś",
            // to nie możemy zacząć w przeszłości.
            return machineReadyAt.isAfter(now) ? Timestamp.from(machineReadyAt) : Timestamp.from(now);
        }
    }

    private Timestamp calculateEndDate(Timestamp startDate, List<OrderItem> orderItemList) {
        double totalMinutesToProduce = 0;

        for (OrderItem item : orderItemList) {
            Double timePerUnit = item.getProduct().getMinutesToProduceOnePiece();
            // Zabezpieczenie przed nullami
            if (timePerUnit == null) timePerUnit = 0.0;

            totalMinutesToProduce += timePerUnit * item.getQty();
        }

        // Dodajemy minuty do daty startu
        Instant start = startDate.toInstant();
        Instant end = start.plus((long) totalMinutesToProduce, ChronoUnit.MINUTES);

        return Timestamp.from(end);
    }
}