CREATE SCHEMA IF NOT EXISTS sales;

CREATE TABLE sales.sales (
    id BIGSERIAL PRIMARY KEY,
    invoice_number VARCHAR(50) NOT NULL UNIQUE,
    customer_id BIGINT NOT NULL,
    total_amount DECIMAL(12, 2) NOT NULL,
    tax_amount DECIMAL(12, 2) NOT NULL DEFAULT 0,
    discount_amount DECIMAL(12, 2) NOT NULL DEFAULT 0,
    final_amount DECIMAL(12, 2) NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'COMPLETED',
    active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE sales.sale_items (
    id BIGSERIAL PRIMARY KEY,
    sale_id BIGINT NOT NULL REFERENCES sales.sales(id),
    product_sku VARCHAR(50) NOT NULL,
    product_name VARCHAR(200) NOT NULL,
    quantity INTEGER NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    subtotal DECIMAL(12, 2) NOT NULL
);

CREATE INDEX idx_sales_customer ON sales.sales(customer_id);
CREATE INDEX idx_sales_invoice ON sales.sales(invoice_number);
CREATE INDEX idx_sales_created ON sales.sales(created_at);
CREATE INDEX idx_sale_items_sale ON sales.sale_items(sale_id);
CREATE INDEX idx_sale_items_product ON sales.sale_items(product_sku);
