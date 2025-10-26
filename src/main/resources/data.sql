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
