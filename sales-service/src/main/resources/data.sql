INSERT INTO sales_orders (id, reference, sales_channel, status, total_amount, created_at)
SELECT gen_random_uuid(), 'SO-2001', 'STORE', 'CONFIRMED', 3200.00, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM sales_orders WHERE reference = 'SO-2001');
