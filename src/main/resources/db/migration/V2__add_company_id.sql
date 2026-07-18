ALTER TABLE sales.sales ADD COLUMN company_id BIGINT NOT NULL DEFAULT 1;
ALTER TABLE sales.sale_items ADD COLUMN company_id BIGINT NOT NULL DEFAULT 1;

CREATE INDEX idx_sales_company ON sales.sales(company_id);
CREATE INDEX idx_sale_items_company ON sales.sale_items(company_id);

CREATE UNIQUE INDEX idx_sales_invoice_company ON sales.sales(company_id, invoice_number);
