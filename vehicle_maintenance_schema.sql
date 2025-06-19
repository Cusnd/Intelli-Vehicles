
-- userinfo
CREATE TABLE `user` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `username` VARCHAR(50) NOT NULL UNIQUE,
  `password` VARCHAR(100) NOT NULL,
  `role` ENUM('admin', 'user') DEFAULT 'user',
  `email` VARCHAR(100),
  `phone` VARCHAR(20),
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- VehicleInfo
CREATE TABLE `vehicle` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `license_plate` VARCHAR(20) NOT NULL UNIQUE,
  `vin` VARCHAR(50) NOT NULL UNIQUE,
  `brand` VARCHAR(50),
  `model` VARCHAR(50),
  `owner_name` VARCHAR(50),
  `owner_phone` VARCHAR(20),
  `user_id` BIGINT,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE SET NULL,
  INDEX (`user_id`)
);

-- MaintenanceRecord
CREATE TABLE `maintenance_record` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `vehicle_id` BIGINT NOT NULL,
  `service_date` DATE NOT NULL,
  `cost` DECIMAL(10, 2) DEFAULT 0.00,
  `service_person` VARCHAR(50),
  `remarks` TEXT,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (`vehicle_id`) REFERENCES `vehicle`(`id`) ON DELETE CASCADE,
  INDEX (`vehicle_id`)
);

-- MaintenanceProject
CREATE TABLE `maintenance_item` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `record_id` BIGINT NOT NULL,
  `item_name` VARCHAR(100) NOT NULL,
  `item_cost` DECIMAL(10, 2),
  FOREIGN KEY (`record_id`) REFERENCES `maintenance_record`(`id`) ON DELETE CASCADE,
  INDEX (`record_id`)
);

-- log
CREATE TABLE `log` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `user_id` BIGINT,
  `operation` VARCHAR(100),
  `ip_address` VARCHAR(45),
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE SET NULL
);
