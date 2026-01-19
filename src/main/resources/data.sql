
INSERT INTO customer (name, phone, email) VALUES
('Jan Kowalski', '123-456-789', 'jan.kowalski@example.com'),
('Anna Nowak', '987-654-321', 'anna.nowak@example.com');

INSERT INTO product (name, description, minutes_to_produce) VALUES
('Kabel USB-C 2m', 'Kabel do ładowania USB-C o długości 2 metry', 1),
('Przedłużacz 5m', 'Przedłużacz elektryczny 5 metrów, 3 gniazda', 1);

INSERT INTO component (name, unit, cost_per_unit) VALUES
('Miedź 1mm', 'meter', 0.50),
('PVC izolacja', 'meter', 0.20),
('Wtyk USB-C', 'qty', 1.50),
('Wtyk elektryczny', 'qty', 2.00),
('Gniazdo elektryczne', 'qty', 3.50);


INSERT INTO bill_of_material (product_id, component_id, qty) VALUES
(1, 1, 2),
(1, 2, 2),
(1, 3, 1);


INSERT INTO bill_of_material (product_id, component_id, qty) VALUES
(2, 1, 5),
(2, 2, 5),
(2, 4, 1),
(2, 5, 3);


INSERT INTO inventory_item (component_id, qty_available, qty_reserved) VALUES
(2, 200, 10),  -- Miedź
(1, 300, 10),  -- PVC
(4, 50, 5),   -- Wtyk USB-C
(3, 30, 0),   -- Wtyk elektryczny
(5, 60, 0);   -- Gniazdo


INSERT INTO stock_reservation (order_id, component_id, qty, status)
VALUES
(1001, 1, 10, 'FROZEN'),  -- zarezerwowane 10 metrów miedzi
(1001, 2, 10, 'FROZEN'),
(1001, 3, 5, 'FROZEN'),
(1002, 5, 3, 'RELEASED'); -- rezerwacja zwolniona


INSERT INTO customer_order (customer_id, status) VALUES
(1,  'NEW'),
(2, 'COMPLETED');

INSERT INTO customer_order_item (order_id, product_id, qty) VALUES
(1, 1, 10),  -- 10 kabli zasilających
(2, 2, 5);   -- 5 przedłużaczy 3m


INSERT INTO work_order (product_id, qty, status, created_at, started_at, finished_at) VALUES
(1, 10, 'PLANNED', '2025-01-05 08:15:00', NULL, NULL),
(2,  45.00, 'FINISHED', '2025-01-04 09:30:00', NULL, NULL);



INSERT INTO procurement (component_id, name, phone, email, lead_time_days) VALUES
  (1,'Alpha Supplies Sp. z o.o.', '+48 22 123 45 67', 'kontakt@alpha-supplies.pl', 5.50),
  (2,'Best Materials Ltd.', '+48 501 234 567', 'sales@bestmaterials.com', 3.25),
  (3,'Tech Components', '+48 22 765 43 21', 'info@tech-components.eu', 7.00),
  (4,'Green Solutions', '+48 600 700 800', 'orders@greensolutions.pl', 6.50),
  (5,'Wide Logistics', '+48 799 888 999', 'contact@widelogistics.com', 2.75);


INSERT INTO purchase_order (vendor_id, status, created_at, sent_at, received_at) VALUES
    (1, 'draft', '2025-01-05 10:00:00', NULL, NULL),
    (2, 'sent', '2025-01-07 09:30:00', '2025-01-08 14:00:00', NULL),
    (3, 'received', '2025-01-03 15:00:00', '2025-01-04 08:00:00', '2025-01-10 12:00:00');

INSERT INTO purchase_order_item (purchase_order_id, component_id, qty) VALUES
    (1, 1, 500),
    (1, 2, 200),
    (2, 3, 50),
    (2, 4, 120),
    (3, 1, 1000),
    (3, 3, 70);


INSERT INTO alerts (status, category, created_at, read_at) VALUES
('UNREAD', 'OPTIMIZATION', '2025-01-10 08:15:00', NULL),
('READ', 'PLACEHOLDER', '2025-01-09 12:30:00', '2025-01-09 14:00:00'),
('UNREAD', 'OPTIMIZATION', '2025-01-11 09:45:00', NULL);

