-- Active: 1667279984414@@127.0.0.1@3306@family_relationship_certificate_db
DROP TABLE IF EXISTS `birth_registration`;
CREATE TABLE `birth_registration` (
    `person_birth_id` INT NOT NULL,
    `email` VARCHAR(64),
    `phone_number` VARCHAR(16),
    `birth_date` DATETIME NOT NULL,
    `reporter_Qualifications` VARCHAR(16) NOT NULL,
    `report_date` DATETIME NOT NULL,
    `citizen_id` INT NOT NULL,
    `place_birth_id` INT NOT NULL,
    `address_id` INT NOT NULL,
    PRIMARY KEY (`person_birth_id`),
    KEY `fk_citizen_taget` (`person_birth_id`),
    KEY `fk_citizen_relation` (`citizen_id`),
    KEY `fk_place_birth` (`place_birth_id`),
    KEY `fk_address` (`address_id`),
    CONSTRAINT `fk_citizen_taget` FOREIGN KEY (`person_birth_id`) REFERENCES `citizen` (`citizen_id`),
    CONSTRAINT `fk_citizen_relation` FOREIGN KEY (`citizen_id`) REFERENCES `citizen` (`citizen_id`),
    CONSTRAINT `fk_place_birth` FOREIGN KEY (`place_birth_id`) REFERENCES `place_birth` (`place_birth_id`),
    CONSTRAINT `fk_address` FOREIGN KEY (`address_id`) REFERENCES `address` (`address_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;