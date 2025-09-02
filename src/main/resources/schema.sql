-- Schemat bazy dla Sprint 1 — CableERP

-- Klienci
CREATE TABLE IF NOT EXISTS customer (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(100) UNIQUE NOT NULL
);

-- Produkty
CREATE TABLE IF NOT EXISTS product (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT
);

-- Komponenty
CREATE TABLE IF NOT EXISTS component (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    unit VARCHAR(20) NOT NULL,
    cost_per_unit NUMERIC(10,2) NOT NULL CHECK (cost_per_unit >= 0)
);

-- Bill of Material
CREATE TABLE IF NOT EXISTS bill_of_material (
    id SERIAL PRIMARY KEY,
    product_id INT NOT NULL REFERENCES product(id) ON DELETE CASCADE,
    component_id INT NOT NULL REFERENCES component(id) ON DELETE CASCADE,
    qty NUMERIC(10,2) NOT NULL CHECK (qty > 0),
    UNIQUE (product_id, component_id)
);
