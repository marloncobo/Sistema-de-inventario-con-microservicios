INSERT INTO categories (id, name, description)
SELECT gen_random_uuid(), 'Electronics', 'Electronic devices and accessories'
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Electronics');

INSERT INTO categories (id, name, description)
SELECT gen_random_uuid(), 'Home', 'Home and lifestyle products'
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Home');

INSERT INTO catalog_products (id, sku, name, description, category_id, unit_price, reorder_level, active)
SELECT gen_random_uuid(), 'SKU-LAP-001', 'Laptop', '14-inch business laptop', id, 3200.00, 10, TRUE
FROM categories WHERE name = 'Electronics'
AND NOT EXISTS (SELECT 1 FROM catalog_products WHERE sku = 'SKU-LAP-001');

INSERT INTO catalog_products (id, sku, name, description, category_id, unit_price, reorder_level, active)
SELECT gen_random_uuid(), 'SKU-CAF-001', 'Coffee Maker', '12-cup programmable coffee maker', id, 280.00, 8, TRUE
FROM categories WHERE name = 'Home'
AND NOT EXISTS (SELECT 1 FROM catalog_products WHERE sku = 'SKU-CAF-001');
