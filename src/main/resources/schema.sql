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
    component_id BIGINT NOT NULL UNIQUE REFERENCES component(id) ON DELETE CASCADE,
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
