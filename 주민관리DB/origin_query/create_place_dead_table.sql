-- Active: 1667279984414@@127.0.0.1@3306@family_relationship_certificate_db
DROP TABLE IF EXISTS `place_dead`;
CREATE TABLE `place_dead` (
    `place_dead_id` INT NOT NULL AUTO_INCREMENT,
    `place_dead_ division` VARCHAR(32),
    PRIMARY KEY (`place_dead_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;