-- Klienci
INSERT INTO customer (name, phone, email)
VALUES
('Jan Kowalski', '123-456-789', 'jan.kowalski@example.com'),
('Anna Nowak', '987-654-321', 'anna.nowak@example.com');

-- Produkty
INSERT INTO product (name, description)
VALUES
('Kabel USB-C 2m', 'Kabel do ładowania USB-C o długości 2 metry'),
('Przedłużacz 5m', 'Przedłużacz elektryczny 5 metrów, 3 gniazda');

-- Komponenty
INSERT INTO component (name, unit, cost_per_unit)
VALUES
('Miedź 1mm', 'meter', 0.50),
('PVC izolacja', 'meter', 0.20),
('Wtyk USB-C', 'qty', 1.50),
('Wtyk elektryczny', 'qty', 2.00),
('Gniazdo elektryczne', 'qty', 3.50);

-- BOM dla produktu Kabel USB-C 2m
INSERT INTO bill_of_material (product_id, component_id, qty)
VALUES
(1, 1, 2),  -- 2 metry miedzi
(1, 2, 2),  -- 2 metry izolacji
(1, 3, 1);  -- 1 szt wtyku USB-C

-- BOM dla produktu Przedłużacz 5m
INSERT INTO bill_of_material (product_id, component_id, qty)
VALUES
(2, 1, 5),  -- 5 metrów miedzi
(2, 2, 5),  -- 5 metrów izolacji
(2, 4, 1),  -- 1 szt wtyku elektrycznego
(2, 5, 3);  -- 3 szt gniazd elektrycznych
