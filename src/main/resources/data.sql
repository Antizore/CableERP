-- ================================
-- FAZA 1 — DANE BAZOWE
-- ================================
INSERT INTO customer (name, phone, email) VALUES
('Jan Kowalski', '123-456-789', 'jan.kowalski@example.com'),
('Anna Nowak', '987-654-321', 'anna.nowak@example.com');

INSERT INTO product (name, description) VALUES
('Kabel USB-C 2m', 'Kabel do ładowania USB-C o długości 2 metry'),
('Przedłużacz 5m', 'Przedłużacz elektryczny 5 metrów, 3 gniazda');

INSERT INTO component (name, unit, cost_per_unit) VALUES
('Miedź 1mm', 'meter', 0.50),
('PVC izolacja', 'meter', 0.20),
('Wtyk USB-C', 'qty', 1.50),
('Wtyk elektryczny', 'qty', 2.00),
('Gniazdo elektryczne', 'qty', 3.50);

-- BOM: Kabel USB-C 2m
INSERT INTO bill_of_material (product_id, component_id, qty) VALUES
(1, 1, 2),
(1, 2, 2),
(1, 3, 1);

-- BOM: Przedłużacz 5m
INSERT INTO bill_of_material (product_id, component_id, qty) VALUES
(2, 1, 5),
(2, 2, 5),
(2, 4, 1),
(2, 5, 3);

-- ================================
-- FAZA 2 — DANE MAGAZYNOWE
-- ================================
INSERT INTO inventory_item (component_id, qty_available, qty_reserved) VALUES
(1, 200, 10),  -- Miedź
(2, 300, 10),  -- PVC
(3, 50, 5),   -- Wtyk USB-C
(4, 30, 0),   -- Wtyk elektryczny
(5, 60, 0);   -- Gniazdo

-- ================================
-- FAZA 2 — TESTOWE REZERWACJE
-- ================================
INSERT INTO stock_reservation (order_id, component_id, qty, status)
VALUES
(1001, 1, 10, 'FROZEN'),  -- zarezerwowane 10 metrów miedzi
(1001, 2, 10, 'FROZEN'),
(1001, 3, 5, 'FROZEN'),
(1002, 5, 3, 'RELEASED'); -- rezerwacja zwolniona

-- ========================
--  Faza 3 — Zamówienia klienta
-- ========================

INSERT INTO customer_order (customer_id, order_number, status) VALUES
(1, 'ORD-2025-0001', 'NEW'),
(2, 'ORD-2025-0002', 'NEW');

INSERT INTO customer_order_item (order_id, product_id, qty) VALUES
(1, 1, 10),  -- 10 kabli zasilających
(2, 2, 5);   -- 5 przedłużaczy 3m

-- ========================
--  Faza 4 — Work Order
-- ========================

INSERT INTO work_order (product_id, qty, status, created_at, started_at, finished_at) VALUES
(1, 10, 'PLANNED', '2025-01-05 08:15:00', NULL, NULL),
(2,  45.00, 'PLANNED', '2025-01-04 09:30:00', NULL, NULL);
