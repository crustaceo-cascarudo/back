-- Users
INSERT INTO `user` (name, password, role) VALUES
    ('admin', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYb1vT1Qzd6', 'ADMIN'),  -- password: admin123
    ('user1', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYb1vT1Qzd6', 'NORMAL'), -- password: user123
    ('user2', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYb1vT1Qzd6', 'NORMAL'), -- password: user123
    ('user3', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYb1vT1Qzd6', 'NORMAL'); -- password: user123

-- Categories
INSERT INTO `category` (name, slug, description, estado) VALUES
    ('Electronics', 'electronics', 'Electronic devices and accessories', TRUE),
    ('Books', 'books', 'Physical and digital books', TRUE),
    ('Clothing', 'clothing', 'Fashion and apparel', TRUE),
    ('Home & Garden', 'home-garden', 'Home improvement and garden supplies', TRUE),
    ('Sports', 'sports', 'Sports equipment and accessories', TRUE),
    ('Accessories', 'accessories', 'Small accessories and peripherals', TRUE),
    ('Toys', 'toys', 'Toys for all ages', TRUE);
    
-- Products
INSERT INTO `product` (name, base_price, discount_percentage, image) VALUES
    ('Smartphone X', 699.99, 0.00, 'products/smartphone-x.jpg'),
    ('4K Ultra HD TV', 999.00, 10.00, 'products/4k-tv.jpg'),
    ('Mystery Novel Y', 14.99, 0.00, 'products/mystery-novel-y.jpg'),
    ('Running Shoes Pro', 89.50, 15.00, 'products/running-shoes-pro.jpg'),
    ('Pizza Margherita', 12.99, 0.00, 'products/pizza-margherita.jpg'),
    ('Burger Classic', 8.50, 5.00, 'products/burger-classic.jpg');

-- Ingredients
INSERT INTO `ingredient` (name, price, image) VALUES
    ('Tomato', 2.50, 'ingredients/tomato.jpg'),
    ('Cheese', 5.00, 'ingredients/cheese.jpg'),
    ('Lettuce', 1.50, 'ingredients/lettuce.jpg'),
    ('Onion', 1.00, 'ingredients/onion.jpg'),
    ('Pepperoni', 8.00, 'ingredients/pepperoni.jpg'),
    ('Bacon', 6.50, 'ingredients/bacon.jpg'),
    ('Mushroom', 3.00, 'ingredients/mushroom.jpg');

-- Product-Category relationships
INSERT INTO `product_category` (product_id, category_id) VALUES
    ((SELECT id FROM `product` WHERE name = 'Smartphone X'), (SELECT id FROM `category` WHERE slug = 'electronics')),
    ((SELECT id FROM `product` WHERE name = '4K Ultra HD TV'), (SELECT id FROM `category` WHERE slug = 'electronics')),
    ((SELECT id FROM `product` WHERE name = 'Mystery Novel Y'), (SELECT id FROM `category` WHERE slug = 'books')),
    ((SELECT id FROM `product` WHERE name = 'Running Shoes Pro'), (SELECT id FROM `category` WHERE slug = 'sports')),
    ((SELECT id FROM `product` WHERE name = 'Running Shoes Pro'), (SELECT id FROM `category` WHERE slug = 'accessories')),
    ((SELECT id FROM `product` WHERE name = 'Pizza Margherita'), (SELECT id FROM `category` WHERE slug = 'home-garden')),
    ((SELECT id FROM `product` WHERE name = 'Burger Classic'), (SELECT id FROM `category` WHERE slug = 'home-garden'));

-- Product-Ingredient relationships
INSERT INTO `product_ingredient` (product_id, ingredient_id) VALUES
    ((SELECT id FROM `product` WHERE name = 'Pizza Margherita'), (SELECT id FROM `ingredient` WHERE name = 'Tomato')),
    ((SELECT id FROM `product` WHERE name = 'Pizza Margherita'), (SELECT id FROM `ingredient` WHERE name = 'Cheese')),
    ((SELECT id FROM `product` WHERE name = 'Burger Classic'), (SELECT id FROM `ingredient` WHERE name = 'Lettuce')),
    ((SELECT id FROM `product` WHERE name = 'Burger Classic'), (SELECT id FROM `ingredient` WHERE name = 'Onion')),
    ((SELECT id FROM `product` WHERE name = 'Burger Classic'), (SELECT id FROM `ingredient` WHERE name = 'Tomato'));