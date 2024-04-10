-- Active: 1667279984414@@127.0.0.1@3306@family_relationship_certificate_db
DROP TABLE IF EXISTS `trans_address_record`;
CREATE TABLE `trans_address_record` (
    `report_date` DATETIME NOT NULL,
    `citizen_id` INT NOT NULL,
    `address_id` INT NOT NULL,
    KEY `fk_trans_address_citizen` (`citizen_id`),
    KEY `fk_trans_address_record_address` (`address_id`),
    CONSTRAINT `fk_trans_address_citizen` FOREIGN KEY (`citizen_id`) REFERENCES `citizen` (`citizen_id`),
    CONSTRAINT `fk_trans_address_record_address` FOREIGN KEY (`address_id`) REFERENCES `address` (`address_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;