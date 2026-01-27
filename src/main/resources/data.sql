INSERT INTO customer (name, phone, email) VALUES
                                              ('John Smith', '+1-202-555-0147', 'john.smith@example.com'),
                                              ('Anna Brown', '+44-7700-900123', 'anna.brown@example.com');

INSERT INTO product (name, description, minutes_to_produce) VALUES
                                                                ('USB-C Charging Cable 2m', 'USB-C charging cable, length 2 meters', 1.25),
                                                                ('Power Extension Cord 5m', '5 meter power extension cord with 3 sockets', 2.50);

INSERT INTO component (name, unit, cost_per_unit) VALUES
                                                      ('Copper Wire 1mm', 'meter', 0.50),
                                                      ('PVC Insulation', 'meter', 0.20),
                                                      ('USB-C Connector', 'qty', 1.50),
                                                      ('Power Plug', 'qty', 2.00),
                                                      ('Power Socket', 'qty', 3.50);

INSERT INTO bill_of_material (product_id, component_id, qty) VALUES
                                                                 (1, 1, 2.00),  -- Copper Wire
                                                                 (1, 2, 2.00),  -- PVC Insulation
                                                                 (1, 3, 1.00);  -- USB-C Connector


INSERT INTO bill_of_material (product_id, component_id, qty) VALUES
                                                                 (2, 1, 5.00),  -- Copper Wire
                                                                 (2, 2, 5.00),  -- PVC Insulation
                                                                 (2, 4, 1.00),  -- Power Plug
                                                                 (2, 5, 3.00);  -- Power Sockets


INSERT INTO inventory_item (component_id, qty_available, qty_reserved) VALUES
                                                                           (1, 300.00, 10.00), -- Copper Wire
                                                                           (2, 200.00, 10.00), -- PVC Insulation
                                                                           (3, 50.00, 5.00),   -- USB-C Connector
                                                                           (4, 30.00, 0.00),   -- Power Plug
                                                                           (5, 60.00, 0.00);   -- Power Socket


INSERT INTO customer_order (
    customer_id,
    status,
    planned_start_at,
    planned_end_at,
    started_at,
    finished_at,
    created_at,
    updated_at
) VALUES
-- NEW order (has plan, not started yet)
(
    1,
    'NEW',
    '2025-01-15 08:00:00',
    '2025-01-15 12:00:00',
    NULL,
    NULL,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
),

-- COMPLETED order (all timestamps filled)
(
    2,
    'COMPLETED',
    '2025-01-05 09:00:00',
    '2025-01-05 15:00:00',
    '2025-01-05 09:10:00',
    '2025-01-05 14:45:00',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

INSERT INTO customer_order_item (order_id, product_id, qty) VALUES
                                                                (1, 1, 10.00), -- 10 USB-C cables
                                                                (2, 2, 5.00);  -- 5 extension cords



INSERT INTO stock_reservation (order_id, component_id, qty, status) VALUES
                                                                        (1, 1, 20.00, 'FROZEN'),   -- Copper Wire for USB-C cables
                                                                        (1, 2, 20.00, 'FROZEN'),   -- PVC Insulation
                                                                        (1, 3, 10.00, 'FROZEN'),   -- USB-C Connectors
                                                                        (2, 5, 3.00, 'RELEASED');  -- Released Power Socket reservation


INSERT INTO procurement (component_id, name, phone, email, lead_time_days) VALUES
                                                                               (1, 'Alpha Supplies Ltd.', '+48 22 123 45 67', 'sales@alpha-supplies.com', 5.5),
                                                                               (2, 'Best Materials Ltd.', '+48 501 234 567', 'contact@bestmaterials.com', 3.25),
                                                                               (3, 'Tech Components Europe', '+48 22 765 43 21', 'info@techcomponents.eu', 7.0),
                                                                               (4, 'Green Energy Solutions', '+48 600 700 800', 'orders@greenenergy.com', 6.5),
                                                                               (5, 'Wide Logistics Group', '+48 799 888 999', 'contact@widelogistics.com', 2.75);


INSERT INTO purchase_order (vendor_id, status, created_at, sent_at, received_at) VALUES
                                                                                     (1, 'draft',    '2025-01-05 10:00:00', NULL, NULL),
                                                                                     (2, 'sent',     '2025-01-07 09:30:00', '2025-01-08 14:00:00', NULL),
                                                                                     (3, 'received', '2025-01-03 15:00:00', '2025-01-04 08:00:00', '2025-01-10 12:00:00');


INSERT INTO purchase_order_item (purchase_order_id, component_id, qty) VALUES
                                                                           (1, 1, 500.00),
                                                                           (1, 2, 200.00),
                                                                           (2, 3, 50.00),
                                                                           (2, 4, 120.00),
                                                                           (3, 1, 1000.00),
                                                                           (3, 3, 70.00);


INSERT INTO alerts (status, category, created_at, read_at) VALUES
                                                               ('UNREAD', 'OPTIMIZATION', '2025-01-10 08:15:00', NULL),
                                                               ('READ',   'PLACEHOLDER', '2025-01-09 12:30:00', '2025-01-09 14:00:00'),
                                                               ('UNREAD', 'OPTIMIZATION', '2025-01-11 09:45:00', NULL);
