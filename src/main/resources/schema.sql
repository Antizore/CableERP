-- ================================
-- CLEANUP (dla środowiska testowego)
-- ================================
DROP TABLE IF EXISTS stock_reservation CASCADE;
DROP TABLE IF EXISTS inventory_item CASCADE;
DROP TABLE IF EXISTS bill_of_material CASCADE;
DROP TABLE IF EXISTS component CASCADE;
DROP TABLE IF EXISTS product CASCADE;
DROP TABLE IF EXISTS customer CASCADE;

-- ================================
-- FAZA 1 — PODSTAWOWE TABELKI
-- ================================
CREATE TABLE customer (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(50),
    email VARCHAR(255) UNIQUE
);

CREATE TABLE product (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT
);

CREATE TABLE component (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    unit VARCHAR(50) NOT NULL,
    cost_per_unit NUMERIC(10,2) NOT NULL DEFAULT 0
);

CREATE TABLE bill_of_material (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL REFERENCES product(id) ON DELETE CASCADE,
    component_id BIGINT NOT NULL REFERENCES component(id) ON DELETE CASCADE,
    qty NUMERIC(10,2) NOT NULL CHECK (qty > 0)
);

-- ================================
-- FAZA 2 — MAGAZYN I REZERWACJE
-- ================================
CREATE TABLE inventory_item (
    id BIGSERIAL PRIMARY KEY,
    component_id BIGINT NOT NULL REFERENCES component(id) ON DELETE CASCADE,
    qty_available NUMERIC(10,2) NOT NULL DEFAULT 0 CHECK (qty_available >= 0),
    qty_reserved NUMERIC(10,2) NOT NULL DEFAULT 0 CHECK (qty_reserved >= 0)
);

CREATE TABLE stock_reservation (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT, -- pojawi się w Faza 3
    component_id BIGINT NOT NULL REFERENCES component(id) ON DELETE CASCADE,
    qty NUMERIC(10,2) NOT NULL CHECK (qty > 0),
    status VARCHAR(20) NOT NULL CHECK (status IN ('FROZEN', 'RELEASED')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE product_inventory (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL REFERENCES product(id) ON DELETE CASCADE,
    qty_available NUMERIC(10,2) NOT NULL DEFAULT 0 CHECK (qty_available >= 0),
    qty_reserved NUMERIC(10,2) NOT NULL DEFAULT 0 CHECK (qty_reserved >= 0)
);


-- ========================
--  Faza 3 — Zamówienia klienta
-- ========================

CREATE TABLE customer_order (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT REFERENCES customer(id) ON DELETE CASCADE,
    order_number VARCHAR(50) NOT NULL UNIQUE,
    status VARCHAR(20) CHECK (status IN ('NEW','RESERVED','IN_PRODUCTION','COMPLETED','CANCELLED')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE customer_order_item (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT REFERENCES customer_order(id) ON DELETE CASCADE,
    product_id BIGINT REFERENCES product(id),
    qty NUMERIC(10,2) NOT NULL
);


-- ========================
--  Faza 4 — Work Order
-- ========================


CREATE TABLE work_order (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT REFERENCES product(id),
    qty NUMERIC(10,2) NOT NULL,
    status VARCHAR(20) CHECK (status IN ('PLANNED', 'IN_PROGRESS','FINISHED')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    finished_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- ========================
--  Faza 6 — Vendorzy
-- ========================

CREATE TABLE vendor (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(50),
    email VARCHAR(255) UNIQUE,
    lead_time_days NUMERIC(10,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE purchase_order (
    id BIGSERIAL PRIMARY KEY,
    vendor_id INT NOT NULL REFERENCES vendor(id),
    status VARCHAR(20) NOT NULL CHECK (status IN ('draft', 'sent', 'received', 'cancelled')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    sent_at TIMESTAMP NULL,
    received_at TIMESTAMP NULL
);

CREATE TABLE purchase_order_item (
    id SERIAL PRIMARY KEY,
    purchase_order_id INT NOT NULL REFERENCES purchase_order(id) ON DELETE CASCADE,
    component_id INT NOT NULL REFERENCES component(id),
    qty NUMERIC(12,2) NOT NULL CHECK (qty > 0)
);