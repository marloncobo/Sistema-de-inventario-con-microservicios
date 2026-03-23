INSERT INTO categories (id, name, description)
SELECT gen_random_uuid(), 'Electronics', 'Electronic devices and accessories'
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Electronics');

INSERT INTO products (id, name, category_id, current_stock)
SELECT gen_random_uuid(), 'Laptop', id, 50
FROM categories WHERE name = 'Electronics'
AND NOT EXISTS (SELECT 1 FROM products WHERE name = 'Laptop');
