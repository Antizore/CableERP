-- Dane testowe dla Sprint 1 — CableERP

-- Klienci
INSERT INTO customer (name, phone, email) VALUES
('Jan Kowalski', '600-123-456', 'jan.kowalski@example.com'),
('Anna Nowak', '601-987-654', 'anna.nowak@example.com');

-- Produkty
INSERT INTO product (name, description) VALUES
('Kabel USB-C', 'Kabel do ładowania i przesyłu danych USB-C'),
('Kabel HDMI', 'Kabel HDMI 2m wysokiej jakości');

-- Komponenty
INSERT INTO component (name, unit, cost_per_unit) VALUES
('Miedź 1mm', 'metr', 0.50),
('PVC izolacja', 'metr', 0.20),
('Wtyk USB-C', 'szt', 1.50),
('Wtyk HDMI', 'szt', 2.00);

-- Bill of Material
INSERT INTO bill_of_material (product_id, component_id, qty) VALUES
(1, 1, 1.5),   -- 1,5 m miedzi na USB-C
(1, 2, 1.5),   -- 1,5 m PVC
(1, 3, 1),     -- 1 wtyk USB-C
(2, 1, 2),     -- 2 m miedzi na HDMI
(2, 2, 2),     -- 2 m PVC
(2, 4, 1);     -- 1 wtyk HDMI
