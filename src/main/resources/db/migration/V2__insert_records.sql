INSERT INTO `user` (name, password, role) VALUES
    ('admin', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYb1vT1Qzd6', 'ADMIN'),  -- password: admin123
    ('user1', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYb1vT1Qzd6', 'NORMAL'), -- password: user123
    ('user2', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYb1vT1Qzd6', 'NORMAL'), -- password: user123
    ('user3', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYb1vT1Qzd6', 'NORMAL'); -- password: user123

    INSERT INTO `category` (name, slug, description, estado) VALUES
    ('Electronics', 'electronics', 'Electronic devices and accessories', TRUE),
    ('Books', 'books', 'Physical and digital books', TRUE),
    ('Clothing', 'clothing', 'Fashion and apparel', TRUE),
    ('Home & Garden', 'home-garden', 'Home improvement and garden supplies', TRUE),
    ('Sports', 'sports', 'Sports equipment and accessories', FALSE);