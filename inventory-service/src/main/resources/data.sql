INSERT INTO categories (id, name, description)
SELECT '11111111-1111-1111-1111-111111111111', 'Electronics', 'Electronic devices and accessories'
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Electronics');

INSERT INTO products (id, sku, name, description, category_id, unit_price, reorder_level, active, current_stock)
SELECT 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa1', 'SKU-LAP-001', 'Laptop', '14-inch business laptop', id, 3200.00, 10, TRUE, 50
FROM categories WHERE name = 'Electronics'
AND NOT EXISTS (SELECT 1 FROM products WHERE id = 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa1');
