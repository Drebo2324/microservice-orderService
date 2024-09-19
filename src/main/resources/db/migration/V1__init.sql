CREATE TABLE `orders`
(
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `order_number` VARCHAR(255) DEFAULT NULL,
    `sku` VARCHAR(255),
    `price` DECIMAL(10, 2),
    `quantity` INT
);