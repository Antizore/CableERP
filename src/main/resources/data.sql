-- =================================================================================
-- 1. BASE CONFIG (Customer & Vendors)
-- =================================================================================

INSERT INTO customer (id, name, phone, email) VALUES
    (1, 'Jan Kowalski (Tester)', '+48 111 222 333', 'jan@test.com');

INSERT INTO vendor (id, name, email) VALUES
                                         (1, 'Fast Delivery Corp', 'fast@vendor.com'),       -- Dostawa: 1 dzień
                                         (2, 'Slow Ocean Shipping', 'slow@vendor.com');      -- Dostawa: 7-14 dni

-- =================================================================================
-- SCENARIUSZ 1: "HAPPY PATH" (Towar jest na stanie)
-- Test: Złóż zamówienie na Product ID = 1.
-- Oczekiwany: Status READY, Data Startu = Dzisiaj.
-- =================================================================================

INSERT INTO product (id, name, description, minutes_to_produce) VALUES
    (1, 'Produkt A (Dostępny)', 'Wszystkie części na magazynie', 15.0);

-- Unit: 'qty' (zgodnie z Twoim Enumem)
INSERT INTO component (id, name, unit, cost_per_unit) VALUES
    (10, 'Komponent A (Dużo)', 'qty', 1.00);

INSERT INTO bill_of_material (product_id, component_id, qty) VALUES
    (1, 10, 5.0); -- Produkt A wymaga 5 sztuk Komponentu A

INSERT INTO component_vendor (component_id, vendor_id, lead_time_days, price) VALUES
    (10, 1, 1, 0.90);

-- INVENTORY: Mamy 1000 sztuk, rezerwacja 0.
INSERT INTO inventory_item (component_id, qty_available, qty_reserved) VALUES
    (10, 1000.00, 0.00);


-- =================================================================================
-- SCENARIUSZ 2: "LEAD TIME" (Brak towaru, długi czas dostawy)
-- Test: Złóż zamówienie na Product ID = 2.
-- Oczekiwany: Status WAITING, Data Startu = Dzisiaj + 7 dni.
-- =================================================================================

INSERT INTO product (id, name, description, minutes_to_produce) VALUES
    (2, 'Produkt B (Opóźniony)', 'Brak części, długi Lead Time', 30.0);

INSERT INTO component (id, name, unit, cost_per_unit) VALUES
    (20, 'Komponent B (Brak)', 'qty', 5.00);

INSERT INTO bill_of_material (product_id, component_id, qty) VALUES
    (2, 20, 1.0);

-- VENDOR: Lead Time 7 dni!
INSERT INTO component_vendor (component_id, vendor_id, lead_time_days, price, is_preferred) VALUES
    (20, 2, 7, 4.50, true);

-- INVENTORY: 0 na stanie.
INSERT INTO inventory_item (component_id, qty_available, qty_reserved) VALUES
    (20, 0.00, 0.00);


-- =================================================================================
-- SCENARIUSZ 3: "FIFO TRIGGER" (Kolejkowanie dostaw)
-- Test:
-- 1. Złóż Order na Product ID = 3 (ilość 10 sztuk -> potrzeba 10 komp).
-- 2. Złóż Order na Product ID = 3 (ilość 5 sztuk -> potrzeba 5 komp).
-- 3. Wyślij POST /inventory/receive { "componentId": 30, "qty": 12 }.
-- Oczekiwany: Pierwsze zamówienie READY, drugie nadal WAITING.
-- =================================================================================

INSERT INTO product (id, name, description, minutes_to_produce) VALUES
    (3, 'Produkt C (FIFO)', 'Testowanie kolejki alokacji', 10.0);

INSERT INTO component (id, name, unit, cost_per_unit) VALUES
    (30, 'Komponent C (Zero)', 'qty', 2.00);

INSERT INTO bill_of_material (product_id, component_id, qty) VALUES
    (3, 30, 1.0); -- 1 produkt = 1 komponent

INSERT INTO component_vendor (component_id, vendor_id, lead_time_days, price) VALUES
    (30, 1, 2, 1.80);

-- INVENTORY: 0 na stanie.
INSERT INTO inventory_item (component_id, qty_available, qty_reserved) VALUES
    (30, 0.00, 0.00);


-- =================================================================================
-- SCENARIUSZ 4: "OPTIMIZATION GAP" (Wciskanie w kolejkę)
-- Test: Złóż zamówienie na Product ID = 4.
-- Sytuacja: Mamy w bazie "Blocker Order" zaplanowany na za 5 dni.
-- Oczekiwany: Alert w tabeli 'alerts' sugerujący wciśnięcie Productu 4 TERAZ.
-- =================================================================================

-- 1. Produkt KANDYDAT (Mały, szybki, dostępny)
INSERT INTO product (id, name, description, minutes_to_produce) VALUES
    (4, 'Produkt D (Skoczek)', 'Mały czas produkcji, dostępny od ręki', 20.0);

-- 2. Produkt BLOCKER (Duży, zaplanowany na przyszłość)
INSERT INTO product (id, name, description, minutes_to_produce) VALUES
    (99, 'Produkt BLOCKER', 'Blokuje kolejkę w przyszłości', 120.0);

-- Komponenty
INSERT INTO component (id, name, unit, cost_per_unit) VALUES
                                                          (40, 'Komponent D (Dostępny)', 'qty', 1.00), -- Dla Skoczka
                                                          (99, 'Komponent BLOCKER (Brak)', 'qty', 10.00); -- Dla Blockera

-- BOM
INSERT INTO bill_of_material (product_id, component_id, qty) VALUES
                                                                 (4, 40, 1.0),
                                                                 (99, 99, 1.0);

-- INVENTORY
INSERT INTO inventory_item (component_id, qty_available, qty_reserved) VALUES
                                                                           (40, 100.00, 0.00), -- Skoczek ma z czego robić
                                                                           (99, 0.00, 1.00);   -- Blocker nie ma części (dlatego jest w przyszłości)

-- VENDOR (Blocker ma długi czas dostawy)
INSERT INTO component_vendor (component_id, vendor_id, lead_time_days) VALUES
                                                                           (40, 1, 1),
                                                                           (99, 2, 5); -- 5 dni czekania

-- === WAŻNE: TWORZYMY SZTUCZNE ZAMÓWIENIE BLOKUJĄCE W PRZYSZŁOŚCI ===
-- Poprawiona składnia dla H2: INTERVAL '5' DAY zamiast '5 days'
-- Status musi być WAITING_FOR_COMPONENTS, żeby OptimizationService go wykrył

INSERT INTO customer_order (id, customer_id, status, planned_start_at, planned_end_at, created_at) VALUES
    (999, 1, 'WAITING_FOR_COMPONENTS',
     CURRENT_TIMESTAMP + INTERVAL '5' DAY,                       -- Start za 5 dni
     CURRENT_TIMESTAMP + INTERVAL '5' DAY + INTERVAL '2' HOUR,   -- Koniec 2h później
     CURRENT_TIMESTAMP
    );

-- Rezerwacja dla blockera
INSERT INTO stock_reservation (customer_order_id, component_id, qty, is_fulfilled) VALUES
    (999, 99, 1.0, false); -- Czeka na komponent 99


