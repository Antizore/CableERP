# SimpleERP

## Flow chart for creating new order:

```mermaid

sequenceDiagram
    autonumber
    actor Client
    actor Employee
    participant OS as OrderService
    participant RS as ReservationService
    participant INV as InventoryDB
    participant ES as EstimationService
    participant OPTI as OptimizationService
    participant NS as NotificationService

    Client->>Employee: Places Order (Call/Email)
    Employee->>OS: POST /orders
    activate OS
    
    note over OS: 1. BOM Explosion & Aggregation
    OS->>OS: Calculate total parts needed

    loop For Each Unique Component
        OS->>RS: makeReservation()
        activate RS
        RS->>INV: Check Availability & Reserve
        alt Stock Available
            INV-->>RS: Stock OK
            RS->>RS: Set Fulfilled = TRUE
        else Shortage
            INV-->>RS: Stock Low
            RS->>RS: Set Fulfilled = FALSE
        end
        RS-->>OS: Reservation Saved
        deactivate RS
    end

    note over OS: 2. Status Decision
    OS->>RS: Check missing count
    RS-->>OS: Returns count
    alt Count == 0
        OS->>OS: Status = READY
    else Count > 0
        OS->>OS: Status = WAITING
    end

    note over OS: 3. Estimation (MRP)
    OS->>ES: estimate()
    activate ES
    ES->>ES: Max(MachineQueue, VendorLeadTime)
    ES-->>OS: Return PlannedStart/End
    deactivate ES

    note over OS: 4. Optimization
    OS->>OPTI: checkForOptimization()
    activate OPTI
    

    opt Gap > ProductionTime AND Order is READY
        OPTI->>NS: createAlert()
        activate NS

        deactivate NS
    end
    
    OPTI-->>OS: Done
    deactivate OPTI

    OS-->>Employee: 201 Created JSON
    deactivate OS
    Employee-->>Client: Order Confirmation

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

