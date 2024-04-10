-- Active: 1667279984414@@127.0.0.1@3306@family_relationship_certificate_db
DROP TABLE IF EXISTS `certificate_code`;
CREATE TABLE `certificate_code` (
    `certificate_code_id` INT NOT NULL AUTO_INCREMENT,
    `certificate_code_name` VARCHAR(64) NOT NULL,
    PRIMARY KEY(certificate_code_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;