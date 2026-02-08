# SimpleERP

## Flow chart for creating new order:

```mermaid

sequenceDiagram
    Actor Client
    Actor Employee
    participant OrderService
    participant ReservationService
    participant Inventory
    participant EstimationService
    participant NotificationService

    Client->>Employee: Calling to make an order
    Employee->>OrderService: POST /orders (Product A,B,C)
    OrderService->>OrderService: Explode BOM (Product A,B,C -> Component X, Y, Z)
    
    loop For each Component
        OrderService->>ReservationService: makeReservation()
        ReservationService->>Inventory: Check Availability
        alt Has Stock
            Inventory ->> ReservationService: OK
            ReservationService->>ReservationService: Set Fulfilled = TRUE
        else No Stock
            Inventory ->> ReservationService: Shortage
            ReservationService->>ReservationService: Set Fulfilled = FALSE
        end
    end

    ReservationService->>OrderService: Reservation created
    ReservationService->>ReservationService: Check missing reservations

    alt All Components ready
      OrderService->>OrderService: Set status = READY
    else Missing Components
      OrderService->>OrderService: Set status = WAITING
    end

    OrderService->>EstimationService: estimate()
    EstimationService->>EstimationService: Calculate Max(machineAvailability, vendorsLeadTime)
    EstimationService->>OrderService: Return plannedStart and plannedEnd

    OrderService->>OptimizationService: checkForOptimization()

    opt Candidate is ready for production
      OptimizationService->>OptimizationService: Gap found
      OptimizationService->>NotificationService: createAlert()
    end
    NotificationService->>NotificationService: send alert to production manager that optimization is possible
     
    OrderService->>Employee: 201 Created (Status: WAITING, Date: Future)
    Employee->>Client: Gives estimated timelines with information that the timelines are subject to optimization and early delivery

```


## Gap logic vizualization

Standard MRP systems often rely on a rigid FIFO (First-In, First-Out) strategy. While safe, this approach creates inefficiencies. If a high-priority order is blocked due to missing materials (e.g., waiting for vendor delivery) as shown below, the production machine remains idle, wasting valuable capacity. We can optimize it by actively monitors the schedule for idle time windows and identifies "jumper" candidates (smaller orders that are fully stocked (READY)) and suggest that there is possibility of fitting it within the idle window.


```mermaid

gantt
    title Scenario A: Standard FIFO Allocation (Inefficient)
    dateFormat  YYYY-MM-DD HH:mm
    axisFormat  %H:%M
    
    section Machine Timeline
    Gap (Machine Idle)         :done, gap1, 2025-02-10 08:00, 4h
    Order #1 (Big & Waiting)   :crit, des1, 2025-02-10 12:00, 4h
    
    section New Order Arrives
    Order #2 (Small & Ready)   :active, des2, after des1, 30m

    section System Analysis
    Optimization Opportunity!  :milestone, 2025-02-10 16:30, 0m



```


The system generates a Real-time Alert for the Production Manager, suggesting an immediate schedule override. This allows Order #2 to be executed during the idle time of Order #1, without delaying the main schedule.



```mermaid

gantt
    title Scenario B: Optimized Schedule (Gap Logic Applied)
    dateFormat  YYYY-MM-DD HH:mm
    axisFormat  %H:%M
    
    section Machine Timeline
    Order #2 (Small & Ready)   :active, des2, 2025-02-10 08:00, 30m
    Gap (Reduced Idle Time)    :done, gap1, after des2, 3.5h
    Order #1 (Big & Waiting)   :crit, des1, 2025-02-10 12:00, 4h
    
    section Benefit
    Saved Time                 :milestone, 2025-02-10 16:30, 0m


```

