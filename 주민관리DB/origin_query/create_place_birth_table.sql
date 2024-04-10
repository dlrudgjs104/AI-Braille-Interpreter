-- Active: 1667279984414@@127.0.0.1@3306@family_relationship_certificate_db

DROP TABLE IF EXISTS `place_birth`;
CREATE TABLE `place_birth` (
    `place_birth_id` INT NOT NULL AUTO_INCREMENT,
    `place_name` VARCHAR(44) NOT NULL,
    PRIMARY KEY (`place_birth_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;