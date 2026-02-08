# SimpleERP

During my internship at a manufacturing company, I got task to solve a critical bottleneck: the communication gap between Sales and Production Planning.

Sales representatives lacked real-time visibility into the production queue. To quote a lead time or check if a "rush order" was possible, they had to constantly interrupt Production Managers with phone calls. This manual back-and-forth wasted valuable time and often led to missed optimization opportunities.

I originally prototyped a solution using Excel macros and deep-nested formulas to bridge this gap. It worked, but it eventually grew into a 70MB monolith that was impossible to maintain and scale.

SimpleERP is my initiative to rebuild that logic the right way using Java and Spring Boot. It aims to solve real-world manufacturing problems by providing:

<ol>

<li>Instant Visibility for Sales: Front-line employees get immediate, accurate delivery estimates without needing to consult a manager.

</li>

<li>Automated Hints for Managers: The system proactively identifies "Queue Blocking" and suggests optimizations (e.g., squeezing small orders into idle windows) to maximize efficiency.</li>

</ol>


## Tech Stack

<ul>
<li>Java 24</li>
<li>Spring Boot 3 (Web, Data JPA)</li>
<li>Hibernate (ORM)</li>
<li>H2 Database (Dev/Test) </li>
<li>JUnit 5 & Mockito (Unit & Integration Testing)</li>
<li>Mermaid.js (Documentation & Visualization)</li>

    
</ul>


## Flow chart for creating new order:

When a client places an order, the system performs a deep check of inventory, reserves components, and estimates the delivery date based on the bottleneck (Machine vs. Materials).

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
## When goods arrive

The system reacts to inventory changes in real-time. When goods are received, they are instantly allocated to the oldest waiting orders.

```mermaid

sequenceDiagram
    autonumber
    actor User as Warehouse Employee
    participant IS as InventoryService
    participant RS as ReservationService
    participant OS as OrderService
    participant OPTI as OptimizationService
    participant NS as NotificationService

    User->>IS: /POST (through controller) receive (ComponentID, Qty=50)
    activate IS

    
    note right of IS: 1. Update Physical Stock
    IS->>IS: Inventory.qty += 50


    note right of IS: 2. Trigger Allocation for given component
    IS->>RS: reallocateStockForComponent(ID)
    activate RS
    
    RS->>RS: Fetch Reservations (Sort by CreatedAt ASC)
    
    loop For Each Reservation
        RS->>RS: Check: Available >= Needed?
        
        alt Stock Available
            RS->>RS: Set Fulfilled = TRUE
            RS->>RS: Decrease Available Pool
            
            note right of RS: 3. Check Order Status
            RS->>OS: tryPromoteOrderToReady()
            activate OS
            OS->>OS: Count Missing Parts
            
            alt All Parts Collected (Missing Parts == 0)
                OS->>OS: Set Status = READY
                
                note right of OS: 4. Check for Gap (APS)
                OS->>OPTI: checkForOptimization(Order)
                activate OPTI
                opt Gap > ProductionTime NAD Order is READY
                    OPTI->>NS: createAlert()
                    activate NS
                    deactivate NS
                end
                OPTI-->>OS: Done
                deactivate OPTI
                
            else Still Missing Parts
                OS->>OS: Status remains WAITING
            end
            OS-->>RS: Done
            deactivate OS

        else Insufficient Stock
            RS->>RS: Set Fulfilled = FALSE (or keep unfulfilled)
        end
    end

    RS-->>IS: Allocation Complete
    deactivate RS
    IS-->>User: 200 OK "Goods received"
    deactivate IS



```





## Gap logic vizualization

### Scenario A: Standard FIFO (Inefficient)

The machine sits idle for 4 hours waiting for Order #1.

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

### Scenario B: Optimized Schedule

The system generates a Real-time Alert for the Production Manager, suggesting an immediate schedule override. Order #2 is executed during the idle time.




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





Roadmap

This project is under active development. Besides fixing stuff there and here and also making it more unify (I was actively learning during making this project so there are parts that need fixing and standarization because at that time I thought that was the best idea). Here are the next steps:

<ul>

<li>[ ] Queue Management API: Endpoints for the Production Manager to drag-and-drop orders (accepting optimization suggestions).</li>

<li>[ ] Security: Implementing Spring Security (JWT) for role-based access (Warehouse vs. Production Manager).</li>

<li>[ ] Multi-Machine Support: Logic to handle multiple production lines simultaneously.</li> 

<li>[ ] Dockerization: Containerizing the application for easier deployment.</li>

</ul>



