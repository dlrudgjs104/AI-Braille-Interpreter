-- Active: 1667279984414@@127.0.0.1@3306@family_relationship_certificate_db
DROP TABLE IF EXISTS `household_relation`;
CREATE TABLE `household_relation` (
    `household_relation_id` INT NOT NULL AUTO_INCREMENT,
    `relation_name` VARCHAR(64),
    PRIMARY KEY (`household_relation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;