-- =================================================================================
-- SEED DATA & TEST SCENARIOS
-- =================================================================================

-- 1. Base Configuration (Customers & Vendors)
INSERT INTO customer (id, name, phone, email) VALUES
    (1, 'John Doe (Tester)', '+1 555 0199', 'john@test.com');

INSERT INTO vendor (id, name, email) VALUES
                                         (1, 'Fast Delivery Corp', 'fast@vendor.com'),      -- Lead time: ~1 day
                                         (2, 'Slow Ocean Shipping', 'slow@vendor.com');     -- Lead time: ~7-14 days

-- =================================================================================
-- SCENARIO 1: "HAPPY PATH" (Items In Stock)
-- Action: Place order for Product ID = 1.
-- Expectation: Status READY, Start Date = Today.
-- =================================================================================

INSERT INTO product (id, name, description, minutes_to_produce) VALUES
    (1, 'Product A (In Stock)', 'All components available immediately', 15.0);

INSERT INTO component (id, name, unit, cost_per_unit) VALUES
    (10, 'Component A (Standard)', 'qty', 1.00);

INSERT INTO bill_of_material (product_id, component_id, qty) VALUES
    (1, 10, 5.0); -- Requires 5 units

INSERT INTO component_vendor (component_id, vendor_id, lead_time_days, price) VALUES
    (10, 1, 1, 0.90);

-- Inventory: 1000 available, 0 reserved
INSERT INTO inventory_item (component_id, qty_available, qty_reserved) VALUES
    (10, 1000.00, 0.00);

-- =================================================================================
-- SCENARIO 2: "LEAD TIME" (Out of Stock, Long Wait)
-- Action: Place order for Product ID = 2.
-- Expectation: Status WAITING, Start Date = Today + 7 days (Vendor Lead Time).
-- =================================================================================

INSERT INTO product (id, name, description, minutes_to_produce) VALUES
    (2, 'Product B (Backordered)', 'Missing parts, long lead time', 30.0);

INSERT INTO component (id, name, unit, cost_per_unit) VALUES
    (20, 'Component B (Scarce)', 'qty', 5.00);

INSERT INTO bill_of_material (product_id, component_id, qty) VALUES
    (2, 20, 1.0);

-- Vendor: 7 days lead time
INSERT INTO component_vendor (component_id, vendor_id, lead_time_days, price, is_preferred) VALUES
    (20, 2, 7, 4.50, true);

-- Inventory: 0 available
INSERT INTO inventory_item (component_id, qty_available, qty_reserved) VALUES
    (20, 0.00, 0.00);

-- =================================================================================
-- SCENARIO 3: "FIFO TRIGGER" (Allocation Queue)
-- Action:
-- 1. Order Product 3 (qty: 10).
-- 2. Order Product 3 (qty: 5).
-- 3. Restock Component 30 (qty: 12).
-- Expectation: 1st order becomes READY, 2nd remains WAITING.
-- =================================================================================

INSERT INTO product (id, name, description, minutes_to_produce) VALUES
    (3, 'Product C (FIFO Test)', 'Testing allocation queue logic', 10.0);

INSERT INTO component (id, name, unit, cost_per_unit) VALUES
    (30, 'Component C (Empty)', 'qty', 2.00);

INSERT INTO bill_of_material (product_id, component_id, qty) VALUES
    (3, 30, 1.0);

INSERT INTO component_vendor (component_id, vendor_id, lead_time_days, price) VALUES
    (30, 1, 2, 1.80);

-- Inventory: 0 available
INSERT INTO inventory_item (component_id, qty_available, qty_reserved) VALUES
    (30, 0.00, 0.00);

-- =================================================================================
-- SCENARIO 4: "OPTIMIZATION GAP" (Schedule Filling)
-- Action: Order Product 4 (Small, fast).
-- Context: There is a "Blocker Order" scheduled 5 days from now.
-- Expectation: System alerts to schedule Product 4 NOW to fill the gap.
-- =================================================================================

-- 1. Product CANDIDATE (Small, fast, available)
INSERT INTO product (id, name, description, minutes_to_produce) VALUES
    (4, 'Product D (Filler)', 'Quick production, components in stock', 20.0);

-- 2. Product BLOCKER (Large, scheduled for future)
INSERT INTO product (id, name, description, minutes_to_produce) VALUES
    (99, 'Product BLOCKER', 'Blocks the queue in the future', 120.0);

-- Components
INSERT INTO component (id, name, unit, cost_per_unit) VALUES
                                                          (40, 'Component D (Available)', 'qty', 1.00),
                                                          (99, 'Component BLOCKER (Missing)', 'qty', 10.00);

-- BOM
INSERT INTO bill_of_material (product_id, component_id, qty) VALUES
                                                                 (4, 40, 1.0),
                                                                 (99, 99, 1.0);

-- Inventory
INSERT INTO inventory_item (component_id, qty_available, qty_reserved) VALUES
                                                                           (40, 100.00, 0.00), -- Filler has stock
                                                                           (99, 0.00, 1.00);   -- Blocker has no stock

-- Vendor
INSERT INTO component_vendor (component_id, vendor_id, lead_time_days) VALUES
                                                                           (40, 1, 1),
                                                                           (99, 2, 5); -- 5 days lead time for blocker

-- === CREATE EXISTING FUTURE ORDER ===
-- Simulates an order waiting for components, scheduled 5 days from now.
INSERT INTO customer_order (id, customer_id, status, planned_start_at, planned_end_at, created_at) VALUES
    (999, 1, 'WAITING_FOR_COMPONENTS',
     CURRENT_TIMESTAMP + INTERVAL '5' DAY,
     CURRENT_TIMESTAMP + INTERVAL '5' DAY + INTERVAL '2' HOUR,
     CURRENT_TIMESTAMP
    );

-- Reservation for the blocker
INSERT INTO stock_reservation (customer_order_id, component_id, qty, is_fulfilled) VALUES
    (999, 99, 1.0, false);