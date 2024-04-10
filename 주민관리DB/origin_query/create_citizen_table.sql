-- Active: 1667279984414@@127.0.0.1@3306@family_relationship_certificate_db
DROP TABLE IF EXISTS `citizen`;

CREATE TABLE `citizen` (
    `citizen_id` INT NOT NULL AUTO_INCREMENT,
    `citizen_reg_num` BLOB NOT NULL,
    `citizen_name` VARCHAR(32) NOT NULL,
    `citizen_gender` CHAR(1) NOT NULL,
    `citizen_birth` DATETIME NOT NULL,
    `address_id` INT NOT NULL,
    `state_id` INT NOT NULL,
    PRIMARY KEY (`citizen_id`),
    KEY `fk_address` (`address_id`),
    KEY `fk_state` (`state_id`),
    CONSTRAINT `fk_citizen_address` FOREIGN KEY (`address_id`) REFERENCES `address` (`address_id`),
    CONSTRAINT `fk_citizen_state` FOREIGN KEY (`state_id`) REFERENCES `state` (`state_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;