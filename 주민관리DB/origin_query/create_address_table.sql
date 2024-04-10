-- Active: 1667279984414@@127.0.0.1@3306@family_relationship_certificate_db
-- Active: 1667279984414@@127.0.0.1@3306@community_db

DROP TABLE IF EXISTS `address`;

CREATE TABLE `address` (
    `address_id` INT NOT NULL AUTO_INCREMENT,
    `address_name` VARCHAR(128) NOT NULL,
    PRIMARY KEY (`address_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;