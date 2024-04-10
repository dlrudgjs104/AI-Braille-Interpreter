-- Active: 1667279984414@@127.0.0.1@3306@family_relationship_certificate_db

DROP TABLE IF EXISTS `state`;

CREATE TABLE `state` (
    `state_id` INT NOT NULL AUTO_INCREMENT,
    `state_name` VARCHAR(128) NOT NULL,
    PRIMARY KEY (`state_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;