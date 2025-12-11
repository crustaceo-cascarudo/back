CREATE TABLE `user` (
    `id` INT(11) NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `role` ENUM('ADMIN', 'NORMAL') NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `sessions` (
    `id` INT(11) NOT NULL AUTO_INCREMENT,
    `user_id` INT(11) NOT NULL,
    `token` VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_sessions_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `category` (
    `id` INT(11) NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL UNIQUE,
    `slug` VARCHAR(255) NOT NULL UNIQUE,
    `description` TEXT,
    `estado` BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `product` (
    `id` INT(11) NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL UNIQUE,
    `base_price` DECIMAL(10, 2) NOT NULL,
    `discount_percentage` DECIMAL(5, 2) DEFAULT 0,
    `image` VARCHAR(255),
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `ingredient` (
    `id` INT(11) NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL UNIQUE,
    `price` DECIMAL(10, 2) NOT NULL,
    `image` VARCHAR(500),
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `product_ingredient` (
    `id` INT(11) NOT NULL AUTO_INCREMENT,
    `ingredient_id` INT(11) NOT NULL,
    `product_id` INT(11) NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`ingredient_id`) REFERENCES `ingredient`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`product_id`) REFERENCES `product`(`id`) ON DELETE CASCADE,
    UNIQUE KEY `unique_product_ingredient` (`product_id`, `ingredient_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `product_category` (
    `id` INT(11) NOT NULL AUTO_INCREMENT,
    `category_id` INT(11) NOT NULL,
    `product_id` INT(11) NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`category_id`) REFERENCES `category`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`product_id`) REFERENCES `product`(`id`) ON DELETE CASCADE,
    UNIQUE KEY `unique_product_category` (`product_id`, `category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

