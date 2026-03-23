
CREATE TABLE IF NOT EXISTS sales_orders (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    reference VARCHAR(100) UNIQUE NOT NULL,
    sales_channel VARCHAR(100) NOT NULL,
    status VARCHAR(50) NOT NULL,
    total_amount NUMERIC(12, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
