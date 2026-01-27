package com.example.CableERP.MRP;

import com.example.CableERP.BillOfMaterials.BillOfMaterialsForEstimate;
import com.example.CableERP.Component.Component;
import com.example.CableERP.Component.ComponentRepository;
import com.example.CableERP.Customer.CustomerOrder.OrderItem;
import com.example.CableERP.Customer.CustomerOrder.OrderRepository;
import com.example.CableERP.Inventory.InventoryRepository;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.*;

@Service
public class Estimation {

    private final InventoryRepository inventoryRepository;
    private final ComponentRepository componentRepository;
    private final OrderRepository orderRepository;

    public Estimation(InventoryRepository inventoryRepository, ComponentRepository componentRepository, OrderRepository orderRepository) {
        this.inventoryRepository = inventoryRepository;
        this.componentRepository = componentRepository;
        this.orderRepository = orderRepository;
    }

    // first one is planned start
    // second one is planned end
    public List<Timestamp> estimate(List<OrderItem> orderItemList) {

        Timestamp plannedStart = finalStart( estimateComponentsAvailiabilityInDaysFromNow(orderItemList),
                estimateMachineAvailability());

        Timestamp plannedEnd = returnApproxRealizationDate(plannedStart,orderItemList);

        return List.of(plannedStart,plannedEnd);

    }



    private Double estimateComponentsAvailiabilityInDaysFromNow(List<OrderItem> orderItemList) {

        Map<Component, Double> summaricOfComponentNeeds = new HashMap<>();
        for (OrderItem orderItem : orderItemList) {

            // prod time orderItem.getProduct().getMinutesToProduceOnePiece() * orderItem.getQty();

            List<BillOfMaterialsForEstimate> listOfNeededComponents = orderItem.getProduct().getBillOfMaterialsList().
                    stream().map(
                            b -> new BillOfMaterialsForEstimate(b.getQty(), new Component(b.getComponent().getName(),
                                    b.getComponent().getUnit(), b.getComponent().getCostPerUnit()))
                    ).toList();

            for (BillOfMaterialsForEstimate bill : listOfNeededComponents) {
                summaricOfComponentNeeds.putIfAbsent(bill.component(), bill.qty() * orderItem.getQty());
                summaricOfComponentNeeds.computeIfPresent(bill.component(), (k, v) -> v += bill.qty()
                        * orderItem.getQty());
            }
        }

        // let's check with inventory how our components are doing and if we have to wait for some of them
        // this is a map containing component and its availability in days
        // 0 means that there are enough components to start production
        // value > 0 means that you need make order for procurement and need to wait for shipment (lead time days)
        Map<Component, Double> componentAvailability = new HashMap<>();

        summaricOfComponentNeeds.forEach(
                (k, v) -> {
                    Component component = componentRepository.findByName(k.getName());
                    Double availableQty = inventoryRepository.findByComponentId(component.getId()).getQtyAvailable();
                    if (v > availableQty) {
                        componentAvailability.put(component, component.getProcurement().getLeadTimeDays());
                    } else {
                        componentAvailability.put(component, 0.);
                    }
                }
        );
        //you can start only when all components are available so need to know te maximum waiting time
        return Collections.max(componentAvailability.values());
    }


    /*

    [Timestamp, boolean]


    2 ways of getting response

    [Timestamp, true] => machine is available now, Timestamp = current time

    [Timestamp, false] => machine is unavailable now, returned Timestamp gives the nearest date when machine
                          will be free

     */
    private List<Object> estimateMachineAvailability() {
        Timestamp lastPlanned = orderRepository.findFirstByPlannedEndAtIsNotNullOrderByPlannedEndAtDesc().getPlannedEndAt();
        Calendar calendar = Calendar.getInstance();
        Timestamp now = new Timestamp(calendar.getTimeInMillis());
        List<Object> response = new ArrayList<>();
        if (now.after(lastPlanned)) {
            response.add(now);
            response.add(true);
        } else {
            response.add(lastPlanned);
            response.add(false);
        }
        return response;
    }


    //take the later one, production cannot run without components and also when the machine is occupied
    private Timestamp finalStart(Double daysFromNow, List<Object> machineAvailability) {
        Date date = new Date();
        Timestamp firstDate = calculateFutureDateFromGivenTimestampAndDays(daysFromNow, new Timestamp(date.getTime()));
        Timestamp secondDate = (Timestamp) machineAvailability.getFirst();
        return (firstDate.after(secondDate)) ? firstDate : secondDate;
    }


    private Timestamp calculateFutureDateFromGivenTimestampAndDays(double givenDays, Timestamp givenDate){
        int days = Integer.parseInt(Double.toString(givenDays).split("\\.")[0]);
        int reminder = Integer.parseInt(Double.toString(givenDays).split("\\.")[1]);
        double hours = reminder * 24.0 / 100.0;
        int wholeHours = Integer.parseInt(Double.toString(hours).split("\\.")[0]);
        int minutes = Integer.parseInt(Double.toString(hours).split("\\.")[1]);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(givenDate.getTime());
        calendar.add(Calendar.DAY_OF_WEEK, days);
        calendar.add(Calendar.HOUR, wholeHours);
        calendar.add(Calendar.MINUTE, minutes);

        return new Timestamp(calendar.getTimeInMillis());
    }


    private Timestamp returnApproxRealizationDate(Timestamp startDate, List<OrderItem> orderItemList){
        double prodtimeInMinutes = 0;
        for (OrderItem orderItem : orderItemList) {
            prodtimeInMinutes += orderItem.getProduct().getMinutesToProduceOnePiece() * orderItem.getQty();
        }

        double prodtimeInDays = prodtimeInMinutes/60.0/24.0;
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_UP);
        String value = (df.format(prodtimeInDays));
        value = value.replace(",",".");

        return calculateFutureDateFromGivenTimestampAndDays(Double.parseDouble(value),startDate);
    }


}
