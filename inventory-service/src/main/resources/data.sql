INSERT INTO categories (id, name, description)
SELECT '11111111-1111-1111-1111-111111111111', 'Electronics', 'Electronic devices and accessories'
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Electronics');

INSERT INTO products (id, name, category_id, current_stock)
SELECT 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa1', 'Laptop', id, 50
FROM categories WHERE name = 'Electronics'
AND NOT EXISTS (SELECT 1 FROM products WHERE name = 'Laptop');
