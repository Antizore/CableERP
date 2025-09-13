-- Usuwamy tabele jeśli istnieją (przy pracy lokalnej, testowej)
DROP TABLE IF EXISTS bill_of_material CASCADE;
DROP TABLE IF EXISTS customer CASCADE;
DROP TABLE IF EXISTS product CASCADE;
DROP TABLE IF EXISTS component CASCADE;

-- Klienci
CREATE TABLE customer (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(50),
    email VARCHAR(255) UNIQUE
);

-- Produkty
CREATE TABLE product (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT
);

-- Komponenty
CREATE TABLE component (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    unit VARCHAR(50) NOT NULL, -- np. szt, metr, kg
    cost_per_unit NUMERIC(10,2) NOT NULL DEFAULT 0
);

-- Bill of Material (relacja N:M product <-> component)
CREATE TABLE bill_of_material (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL REFERENCES product(id) ON DELETE CASCADE,
    component_id BIGINT NOT NULL REFERENCES component(id) ON DELETE CASCADE,
    qty NUMERIC(10,2) NOT NULL CHECK (qty > 0)
);
